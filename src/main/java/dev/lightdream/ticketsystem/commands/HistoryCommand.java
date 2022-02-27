package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.JdaEmbed;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.dto.BanRecord;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryCommand extends DiscordCommand {
    public HistoryCommand() {
        super(Main.instance, "history", "", Permission.BAN_MEMBERS, "[user_id]", true);
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

        Main.instance.bot.retrieveUserById(id).queue(user -> {
            List<BanRecord> bans = Main.instance.databaseManager.getPastBans(id);
            JdaEmbed embed = Main.instance.jdaConfig.bans.clone();
            bans.forEach(ban -> {
                System.out.println(ban.reason);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    Date date = new Date(ban.timestamp);

                    embed.description += Main.instance.jdaConfig.bansEntry
                            .replace("%id%", String.valueOf(ban.id))
                            .replace("%reason%", ban.reason)
                            .replace("%date%", dateFormat.format(date));
                    embed.description += "\n";
            });

            textChannel.sendMessageEmbeds(embed
                    .parse("name", user.getName())
                    .build().build()).queue();
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
