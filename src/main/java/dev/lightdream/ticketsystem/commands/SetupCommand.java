package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.context.GuildCommandContext;
import dev.lightdream.jdaextension.dto.context.PrivateCommandContext;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.dto.TicketGroup;
import net.dv8tion.jda.api.Permission;

import java.util.ArrayList;

public class SetupCommand extends DiscordCommand {

    public SetupCommand() {
        super(Main.instance, "setup", Main.instance.lang.setupCommandDescription, Permission.ADMINISTRATOR, true, new ArrayList<>());
    }

    @Override
    public void executeGuild(GuildCommandContext context) {
        Main.instance.config.ticketGroups.forEach(TicketGroup::sendSetupMessage);

        sendMessage(context, Main.instance.jdaConfig.setupFinished);
    }

    @Override
    public void executePrivate(PrivateCommandContext commandContext) {

    }

    @Override
    public boolean isMemberSafe() {
        return false;
    }
}
