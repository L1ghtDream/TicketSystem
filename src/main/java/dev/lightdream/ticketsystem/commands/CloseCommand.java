package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.JDAExtensionMain;
import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.manager.TicketManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class CloseCommand extends DiscordCommand {
    public CloseCommand(JDAExtensionMain main) {
        super(main, "close", Main.instance.lang.closeCommandDescription, null, "", true);
    }

    @Override
    public void execute(Member member, TextChannel textChannel, List<String> list) {
        TicketManager.closeTicket(textChannel);
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
