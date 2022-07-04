package dev.lightdream.ticketsystem.database;

import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.databasemanager.dto.entry.impl.IntegerDatabaseEntry;
import dev.lightdream.jdaextension.dto.JDAEmbed;
import dev.lightdream.jdaextension.dto.JDAField;
import dev.lightdream.lambda.LambdaExecutor;
import dev.lightdream.logger.Debugger;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.dto.TicketType;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@DatabaseTable(table = "tickets")
public class Ticket extends IntegerDatabaseEntry {

    @DatabaseField(columnName = "type")
    public String type;
    @DatabaseField(columnName = "channel_id")
    public Long channelID;
    @DatabaseField(columnName = "creator_id")
    public Long creatorID;
    @DatabaseField(columnName = "pinged_manager")
    public boolean pingedManager;
    @DatabaseField(columnName = "active")
    public boolean active;
    @DatabaseField(columnName = "timestamp")
    public long timestamp;
    @DatabaseField(columnName = "dialogue_index")
    public int dialogueIndex = 0;
    @DatabaseField(columnName = "dialogue_history")
    public List<String> dialogue = new ArrayList<>();
    @DatabaseField(columnName = "last_question_id")
    public long lastQuestionID;
    @DatabaseField(columnName = "choice_names")
    public List<String> choiceNames = new ArrayList<>();
    @DatabaseField(columnName = "choices")
    public List<String> choices = new ArrayList<>();

    public Ticket(String type, Long channelID, Long creatorID) {
        super(Main.instance);
        this.type = type;
        this.channelID = channelID;
        this.pingedManager = false;
        this.creatorID = creatorID;
        this.active = true;
        this.timestamp = System.currentTimeMillis();
    }

    @SuppressWarnings("unused")
    public Ticket() {
        super(Main.instance);
    }

    public Transcript getTranscript() {
        Transcript transcript = Main.instance.databaseManager.getTranscript(id);
        if (transcript == null) {
            transcript = new Transcript(id);
        }
        return transcript;
    }

    public void close() {
        this.active = false;
        Main.instance.bot.retrieveUserById(creatorID)
                .queue(user -> user.openPrivateChannel()
                        .queue(channel -> channel.sendMessageEmbeds(Main.instance.jdaConfig.closedTicket
                                        .parse("id", String.valueOf(getTranscript().ticketID)).build().build())
                                .queue(null,
                                        new ErrorHandler().handle(ErrorResponse.CANNOT_SEND_TO_USER,
                                                e -> {
                                                    //empty
                                                }))));
        save();
    }

    public void setPingedManager(boolean pingedManager) {
        this.pingedManager = pingedManager;
        save();
    }

    public TextChannel getTextChannel() {
        return Main.instance.bot.getTextChannelById(channelID);
    }

    @SneakyThrows
    public void sendNextDialogueLine() {
        TicketType ticketType = Main.instance.config.getTicketTypeByID(type);
        if (ticketType == null) {
            return;
        }

        if (dialogueIndex == ticketType.questions.size()) {
            endDialogue();
            return;
        }

        AtomicBoolean canContinue = new AtomicBoolean(true);

        String question = handleChoices(null,
                optionChoice -> {
                    int index = choiceNames.indexOf(optionChoice.optionName);

                    if (!optionChoice.optionChoice.equals(choices.get(index))) {
                        dialogueIndex++;
                        canContinue.set(false);
                    }
                }
        );

        if (!canContinue.get()) {
            sendNextDialogueLine();
            return;
        }

        CompletableFuture<Message> message = getTextChannel().sendMessageEmbeds(
                Main.instance.jdaConfig.dialogueLine
                        .parse("number", String.valueOf(dialogueIndex + 1))
                        .parse("question", question)
                        .build().build()
        ).submit();

        this.lastQuestionID = message.get().getIdLong();
        save();
    }

    @SneakyThrows
    public void registerResponse(String response) {
        dialogue.add(response);

        handleChoices(optionChoice -> {
            if (!optionChoice.availableChooses.contains(response)) {
                return;
            }

            choiceNames.add(optionChoice.optionName);
            choices.add(response);
        }, null);

        dialogueIndex++;
        save();
        if (lastQuestionID != 0L) {
            CompletableFuture<Message> message = getTextChannel().retrieveMessageById(lastQuestionID).submit();
            message.get().delete().queue();
        }
        sendNextDialogueLine();
    }

    private void endDialogue() {
        JDAEmbed embed = Main.instance.jdaConfig.dialogueEnd.clone();

        TicketType ticketType = Main.instance.config.getTicketTypeByID(type);

        if (ticketType == null) {
            return;
        }

        List<String> questions = ticketType.questions;
        int j = -1;

        for (int i = 0; i < dialogue.size(); i++) {
            j++;
            AtomicBoolean canContinue = new AtomicBoolean(true);

            String question = handleChoices(null,
                    optionChoice -> {
                        int index = choiceNames.indexOf(optionChoice.optionName);

                        if (!optionChoice.optionChoice.equals(choices.get(index))) {
                            dialogueIndex++;
                            canContinue.set(false);
                        } else {
                            canContinue.set(true);
                        }
                    }, j);

            while (!canContinue.get()) {
                j++;
                question = handleChoices(null,
                        optionChoice -> {
                            int index = choiceNames.indexOf(optionChoice.optionName);

                            if (!optionChoice.optionChoice.equals(choices.get(index))) {
                                dialogueIndex++;
                                canContinue.set(false);
                            } else {
                                canContinue.set(true);
                            }
                        }, j);
            }
            String answer = dialogue.get(i);

            Debugger.log("Question: " + question + " Answer: " + answer);

            embed.fields.add(new JDAField("Q: " + question, "A: " + answer, false));
        }

        getTextChannel().sendMessageEmbeds(embed.build().build()).queue();
    }

    public boolean isDialogueEnded() {
        TicketType ticketType = Main.instance.config.getTicketTypeByID(type);
        if (ticketType == null) {
            return true;
        }
        return dialogueIndex == ticketType.questions.size();
    }

    private String handleChoices(LambdaExecutor.NoReturnLambdaExecutor<OptionChoiceClass> l1,
                                 LambdaExecutor.NoReturnLambdaExecutor<NameChoicePairClass> l2) {
        return handleChoices(l1, l2, dialogueIndex);
    }

    private String handleChoices(LambdaExecutor.NoReturnLambdaExecutor<OptionChoiceClass> l1,
                                 LambdaExecutor.NoReturnLambdaExecutor<NameChoicePairClass> l2,
                                 int dialogueIndex) {
        TicketType ticketType = Main.instance.config.getTicketTypeByID(type);
        if (ticketType == null) {
            return null;
        }

        String question = ticketType.questions.get(dialogueIndex);
        String settings;
        String questionText;

        if (question.split("\\|\\|\\|").length != 1) {
            settings = question.split("\\|\\|\\|")[0];
            questionText = question.split("\\|\\|\\|")[1];

            String settingID = settings.split("~~~")[0];
            String optionName;
            String optionChoice;
            List<String> availableChooses;
            //options~~~option~~~a,b,c|||Question
            if (settingID.equals("options")) {
                optionName = settings.split("~~~")[1];
                availableChooses = Arrays.asList(settings.split("~~~")[2].split(","));

                if (l1 != null) {
                    l1.execute(new OptionChoiceClass(availableChooses, optionName));
                }

                return questionText;
            }
            //option~~~a,b,c|||Question
            optionName = settings.split("~~~")[0];
            optionChoice = settings.split("~~~")[1];

            if (l2 != null) {
                l2.execute(new NameChoicePairClass(optionName, optionChoice));
            }
            return questionText;
        }
        return question;
    }

    @AllArgsConstructor
    private static class OptionChoiceClass {
        public List<String> availableChooses;
        public String optionName;
    }

    @AllArgsConstructor
    private static class NameChoicePairClass {
        public String optionName;
        public String optionChoice;
    }


}
