package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.JDAExtensionMain;
import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.dto.BanRecord;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class UnbanCommand extends DiscordCommand {
    public UnbanCommand(JDAExtensionMain main) {
        super(main, "unban", "description", Permission.BAN_MEMBERS, "[user_id]", true);
    }

    @Override
    public void execute(Member member, TextChannel textChannel, List<String> args) {
        if (args.size() != 1) {
            sendUsage(textChannel);
            return;
        }

        long id;
        try {
            id = Long.parseLong(args.get(0));
        } catch (Exception e) {
            System.out.println("NFE"); //todo
            return;
        }

        BanRecord ban = Main.instance.databaseManager.getBan(id);

        if (ban == null) {
            textChannel.sendMessage("The user is not banned")
                    .queue();
            return;
        }

        if (ban.unban(textChannel)) {

            textChannel.sendMessage("User unbanned")
                    .queue();
        } else {
            textChannel.sendMessage("The user is not banned")
                    .queue();
        }
    }

    @Override
    public void execute(User user, MessageChannel messageChannel, List<String> list) {

    }

    @Override
    public boolean isMemberSafe() {
        return false;
    }
}
