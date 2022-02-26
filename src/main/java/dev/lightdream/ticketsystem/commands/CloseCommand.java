package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.JDAExtensionMain;
import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.ticketsystem.Main;
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
        Main.instance.config.ticketTypes.forEach(ticketType -> {
            if (ticketType.categoryID.equals(textChannel.getParentCategoryIdLong())) {
                textChannel.sendMessageEmbeds(Main.instance.jdaConfig.closingTicket.build().build()).queue();
                new java.util.Timer().schedule(new java.util.TimerTask() {
                    @Override
                    public void run() {
                        textChannel.delete().queue();
                    }
                }, 5000);

            }
        });
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
