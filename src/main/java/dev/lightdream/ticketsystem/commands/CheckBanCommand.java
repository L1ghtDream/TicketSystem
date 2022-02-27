package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.dto.BanRecord;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class CheckBanCommand extends DiscordCommand {
    public CheckBanCommand() {
        super(Main.instance, "checkBan", "", Permission.BAN_MEMBERS, "[user_id]", true);
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

        BanRecord ban = Main.instance.databaseManager.getBan(id);
        if (ban == null) {
            textChannel.sendMessageEmbeds(Main.instance.jdaConfig.notBanned.build().build()).queue();
            return;
        }

        Main.instance.bot.retrieveUserById(id).queue(user ->
                Main.instance.bot.retrieveUserById(ban.bannedBy).queue(bannedBy ->
                        textChannel.sendMessageEmbeds(Main.instance.jdaConfig.unbanDetails
                                .parse("name", user.getName())
                                .parse("id", user.getId())
                                .parse("banned_by_name", bannedBy.getName())
                                .parse("banned_by_id", String.valueOf(bannedBy))
                                .parse("reason", ban.reason)
                                .build().build()
                        ).queue()));
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
