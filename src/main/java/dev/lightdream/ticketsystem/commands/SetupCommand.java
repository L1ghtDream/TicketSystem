package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.Button;
import dev.lightdream.jdaextension.dto.JdaEmbed;
import dev.lightdream.jdaextension.enums.JDAButtonType;
import dev.lightdream.ticketsystem.Main;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class SetupCommand extends DiscordCommand {

    public SetupCommand() {
        super(Main.instance, "setup", "", Permission.ADMINISTRATOR, "", true);
    }

    @Override
    public void execute(Member member, TextChannel textChannel, List<String> list) {
        JdaEmbed ticket = Main.instance.jdaConfig.ticket.clone();
        Main.instance.config.ticketTypes.forEach(ticketType ->
                ticket.buttons.add(new Button(JDAButtonType.PRIMARY, ticketType.id, ticketType.name)));

        ticket.buildMessageAction(Main.instance.bot.getTextChannelById(Main.instance.config.ticketsChanel)).queue();

        JdaEmbed banTicket = Main.instance.jdaConfig.unbanTicket.clone();
        banTicket.buttons.add(new Button(JDAButtonType.PRIMARY, Main.instance.config.unbanTicket.id, Main.instance.config.unbanTicket.name));

        banTicket.buildMessageAction(Main.instance.bot.getTextChannelById(Main.instance.config.unbanTicketsChanel)).queue();

        textChannel.sendMessageEmbeds(Main.instance.jdaConfig.setupFinished.build().build()).queue();
    }

    @Override
    public void execute(User user, MessageChannel messageChannel, List<String> list) {
        //Imposible
    }

    @Override
    public boolean isMemberSafe() {
        return false;
    }
}
