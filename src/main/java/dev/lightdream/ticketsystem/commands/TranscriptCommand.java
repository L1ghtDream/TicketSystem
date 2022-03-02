package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.CommandArgument;
import dev.lightdream.jdaextension.dto.CommandContext;
import dev.lightdream.logger.Debugger;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.dto.Ticket;
import dev.lightdream.ticketsystem.dto.Transcript;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.Collections;

public class TranscriptCommand extends DiscordCommand {
    public TranscriptCommand() {
        super(Main.instance, "transcript", Main.instance.lang.transcriptCommandDescription, null, true, Collections.singletonList(
                new CommandArgument(OptionType.INTEGER, "id", Main.instance.lang.ticketIDDescription, true)
        ));
    }

    @Override
    public void executeGuild(CommandContext context) {

        int id;
        try {
            id = (int) context.getArgument("id").getAsLong();
        } catch (Exception e) {
            sendMessage(context, Main.instance.jdaConfig.invalidTicketID);
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

        if (!ticket.creatorID.equals(context.getMember().getIdLong()) && !context.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            sendMessage(context, Main.instance.jdaConfig.notAllowedToAccessTranscript);
            return;
        }
        transcript.send(context.getUser());
    }

    @Override
    public void executePrivate(CommandContext context) {
        int id;
        try {
            id = (int) context.getArgument("id").getAsLong();
        } catch (Exception e) {
            sendMessage(context, Main.instance.jdaConfig.invalidTicketID);
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

        if (!ticket.creatorID.equals(context.getUser().getIdLong())) {
            sendMessage(context, Main.instance.jdaConfig.notAllowedToAccessTranscript);
            return;
        }
        transcript.send(context.getUser());
    }

    @Override
    public boolean isMemberSafe() {
        return true;
    }
}
