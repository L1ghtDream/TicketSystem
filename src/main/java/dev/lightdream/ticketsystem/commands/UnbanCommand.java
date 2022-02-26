package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.JDAExtensionMain;
import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.dto.BanRecord;
import dev.lightdream.ticketsystem.manager.BanManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class UnbanCommand extends DiscordCommand {
    public UnbanCommand(JDAExtensionMain main) {
        super(main, "unban", Main.instance.lang.unbanCommandDescription, Permission.BAN_MEMBERS, "[user_id]", true);
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
            textChannel.sendMessageEmbeds(Main.instance.jdaConfig.invalidID.build().build()).queue();
            return;
        }

        BanManager.unban(id, textChannel);
    }

    @Override
    public void execute(User user, MessageChannel messageChannel, List<String> list) {

    }

    @Override
    public boolean isMemberSafe() {
        return false;
    }
}
