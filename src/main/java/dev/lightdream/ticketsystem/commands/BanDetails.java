package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.logger.Debugger;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.dto.BanRecord;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class BanDetails extends DiscordCommand {
    public BanDetails() {
        super(Main.instance, "banDetails", "", Permission.BAN_MEMBERS, "[id]", true);
    }

    @Override
    public void execute(Member member, TextChannel textChannel, List<String> args) {
        if (args.size() != 1) {
            sendUsage(textChannel);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(args.get(0));
        } catch (Exception e) {
            textChannel.sendMessageEmbeds(Main.instance.jdaConfig.invalidBanID.build().build()).queue();
            return;
        }

        BanRecord ban = Main.instance.databaseManager.getBan(id);
        if (ban == null) {
            Debugger.info("Null ban");
            return;
        }

        Main.instance.bot.retrieveUserById(ban.user).queue(user ->
                Main.instance.bot.retrieveUserById(ban.bannedBy).queue(bannedBy ->
                        textChannel.sendMessageEmbeds(Main.instance.jdaConfig.unbanDetails
                                .parse("name", user.getName())
                                .parse("id", user.getId())
                                .parse("banned_by_name", bannedBy.getName())
                                .parse("banned_by_id", String.valueOf(bannedBy))
                                .parse("reason", ban.reason)
                                .build().build()).queue()));
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
