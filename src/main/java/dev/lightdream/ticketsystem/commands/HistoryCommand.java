package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.CommandArgument;
import dev.lightdream.jdaextension.dto.CommandContext;
import dev.lightdream.jdaextension.dto.JdaEmbed;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.dto.BanRecord;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HistoryCommand extends DiscordCommand {
    public HistoryCommand() {
        super(Main.instance, "history", Main.instance.lang.historyCommandDescription, Permission.BAN_MEMBERS, true, Collections.singletonList(
                new CommandArgument(OptionType.STRING, "user_id", Main.instance.lang.userIDDescription, true)
        ));
    }

    @Override
    public void executeGuild(CommandContext context) {
        long id;
        try {
            id = Long.parseLong(context.getArgument("user_id").getAsString());
        } catch (Exception e) {
            sendMessage(context, Main.instance.jdaConfig.invalidID);
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

            sendMessage(context, embed
                    .parse("name", user.getName()));
        });
    }

    @Override
    public void executePrivate(CommandContext commandContext) {
        //Impossible
    }

    @Override
    public boolean isMemberSafe() {
        return false;
    }
}
