package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.context.GuildCommandContext;
import dev.lightdream.jdaextension.dto.context.PrivateCommandContext;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.event.TicketCloseEvent;

import java.util.ArrayList;

public class CloseCommand extends DiscordCommand {
    public CloseCommand() {
        super(Main.instance, "close", Main.instance.lang.closeCommandDescription, null, false, new ArrayList<>());
    }

    @Override
    public void executeGuild(GuildCommandContext context) {
        new TicketCloseEvent(context.getTextChannel(), context.getUser(), context.getEvent()).fire();
    }

    @Override
    public void executePrivate(PrivateCommandContext commandContext) {
        //Impossible
    }

    @Override
    public boolean isMemberSafe() {
        return false;
    }
}
