package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.CommandContext;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.manager.TicketManager;

import java.util.ArrayList;

public class CloseCommand extends DiscordCommand {
    public CloseCommand() {
        super(Main.instance, "close", Main.instance.lang.closeCommandDescription, null, false, new ArrayList<>());
    }

    @Override
    public void executeGuild(CommandContext context) {
        sendMessage(context, TicketManager.closeTicket(context.getTextChannel(), context.getUser()));
    }

    @Override
    public void executePrivate(CommandContext commandContext) {
        //Imposible
    }

    @Override
    public boolean isMemberSafe() {
        return false;
    }
}
