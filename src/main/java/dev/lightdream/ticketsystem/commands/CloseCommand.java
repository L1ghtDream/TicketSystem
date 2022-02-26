package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.manager.TicketManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class CloseCommand extends DiscordCommand {
    public CloseCommand() {
        super(Main.instance, "close", "", null, "", true);
    }

    @Override
    public void execute(Member member, TextChannel textChannel, List<String> list) {
        TicketManager.closeTicket(textChannel, member.getUser());
    }

    @Override
    public void execute(User user, MessageChannel messageChannel, List<String> list) {
        //Impossible
    }

    @Override
    public boolean isMemberSafe() {
        return false;
    }
}
