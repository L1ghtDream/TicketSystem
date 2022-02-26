package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.logger.Debugger;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.dto.Ticket;
import dev.lightdream.ticketsystem.dto.Transcript;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class TranscriptCommand extends DiscordCommand {
    public TranscriptCommand() {
        super(Main.instance, "transcript", "See the transcript of a ticket", null, "[id]", true);
    }

    @SneakyThrows
    @Override
    public void execute(Member member, TextChannel textChannel, List<String> args) {
        if (args.size() != 1) {
            sendUsage(textChannel);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(args.get(0));
        } catch (Exception e) {
            textChannel.sendMessageEmbeds(Main.instance.jdaConfig.invalidTicketID.build().build()).queue();
            return;
        }

        Transcript transcript = Main.instance.databaseManager.getTranscript(id);
        if (transcript == null) {
            Debugger.info("Null transcript");
            return;
        }

        Ticket ticket = Main.instance.databaseManager.getTicket(id);

        if (ticket == null) {
            Debugger.info("Null ticket");
            return;
        }

        if (!ticket.creatorID.equals(member.getIdLong()) && !member.hasPermission(Permission.ADMINISTRATOR)) {
            textChannel.sendMessageEmbeds(Main.instance.jdaConfig.notAllowedToAccessTranscript.build().build()).queue();
            return;
        }
        transcript.send(member.getUser());
    }

    @Override
    public void execute(User user, MessageChannel messageChannel, List<String> args) {
        if (args.size() != 1) {
            sendUsage(messageChannel);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(args.get(0));
        } catch (Exception e) {
            messageChannel.sendMessageEmbeds(Main.instance.jdaConfig.invalidTicketID.build().build()).queue();
            return;
        }

        Transcript transcript = Main.instance.databaseManager.getTranscript(id);
        if (transcript == null) {
            return;
        }

        Ticket ticket = Main.instance.databaseManager.getTicket(transcript.ticketID);

        if (ticket == null) {
            return;
        }

        if (!ticket.creatorID.equals(user.getIdLong())) {
            messageChannel.sendMessageEmbeds(Main.instance.jdaConfig.notAllowedToAccessTranscript.build().build()).queue();
            return;
        }
        transcript.send(user);
    }

    @Override
    public boolean isMemberSafe() {
        return true;
    }
}
