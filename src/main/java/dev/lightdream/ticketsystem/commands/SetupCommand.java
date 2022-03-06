package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.Button;
import dev.lightdream.jdaextension.dto.JdaEmbed;
import dev.lightdream.jdaextension.dto.context.GuildCommandContext;
import dev.lightdream.jdaextension.dto.context.PrivateCommandContext;
import dev.lightdream.jdaextension.enums.JDAButtonType;
import dev.lightdream.ticketsystem.Main;
import net.dv8tion.jda.api.Permission;

import java.util.ArrayList;

public class SetupCommand extends DiscordCommand {

    public SetupCommand() {
        super(Main.instance, "setup", Main.instance.lang.setupCommandDescription, Permission.ADMINISTRATOR, true, new ArrayList<>());
    }

    @Override
    public void executeGuild(GuildCommandContext context) {
        JdaEmbed ticket = Main.instance.jdaConfig.ticket.clone();
        Main.instance.config.ticketTypes.forEach(ticketType ->
                ticket.buttons.add(new Button(JDAButtonType.PRIMARY, ticketType.id, ticketType.name)));

        ticket.buildMessageAction(Main.instance.bot.getTextChannelById(Main.instance.config.ticketsChanel)).queue();

        JdaEmbed banTicket = Main.instance.jdaConfig.unbanTicket.clone();
        banTicket.buttons.add(new Button(JDAButtonType.PRIMARY, Main.instance.config.unbanTicket.id, Main.instance.config.unbanTicket.name));

        banTicket.buildMessageAction(Main.instance.bot.getTextChannelById(Main.instance.config.unbanTicketsChanel)).queue();

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
