package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.ticketsystem.Main;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class HistoryCommand extends DiscordCommand {
    public HistoryCommand() {
        super(Main.instance, "history", "", Permission.BAN_MEMBERS, "[user_id]", true);
    }

    @Override
    public void execute(Member member, TextChannel textChannel, List<String> list) {
        textChannel.sendMessage("WIP").queue();
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
