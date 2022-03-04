package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.CommandArgument;
import dev.lightdream.jdaextension.dto.CommandContext;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.dto.BanRecord;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.Collections;

public class CheckBanCommand extends DiscordCommand {
    public CheckBanCommand() {
        super(Main.instance, "checkBan", Main.instance.lang.checkBanCommandDescription, Permission.BAN_MEMBERS, true, Collections.singletonList(
                new CommandArgument(OptionType.STRING, "user_id", Main.instance.lang.banIDDescription, true)
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

        BanRecord ban = Main.instance.databaseManager.getBan(id);
        if (ban == null) {
            sendMessage(context, Main.instance.jdaConfig.notBanned);
            return;
        }

        Main.instance.bot.retrieveUserById(id).queue(user ->
                Main.instance.bot.retrieveUserById(ban.bannedBy).queue(bannedBy ->
                        sendMessage(context, Main.instance.jdaConfig.unbanDetails
                                .parse("name", user.getName())
                                .parse("id", user.getId())
                                .parse("banned_by_name", bannedBy.getName())
                                .parse("banned_by_id", String.valueOf(bannedBy))
                                .parse("reason", ban.reason))));
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
