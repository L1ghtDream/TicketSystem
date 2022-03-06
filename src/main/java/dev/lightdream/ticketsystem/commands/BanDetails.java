package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.CommandArgument;
import dev.lightdream.jdaextension.dto.context.GuildCommandContext;
import dev.lightdream.jdaextension.dto.context.PrivateCommandContext;
import dev.lightdream.logger.Debugger;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.dto.BanRecord;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.Collections;

public class BanDetails extends DiscordCommand {
    public BanDetails() {
        super(Main.instance, "banDetails", Main.instance.lang.banDetailsCommandDescription, Permission.BAN_MEMBERS, true, Collections.singletonList(
                new CommandArgument(OptionType.INTEGER, "id", Main.instance.lang.banIDDescription, true)
        ));
    }

    @Override
    public void executeGuild(GuildCommandContext context) {
        int id;
        try {
            id = (int) context.getArgument("id").getAsLong();
        } catch (Exception e) {
            sendMessage(context, Main.instance.jdaConfig.invalidBanID);
            return;
        }

        BanRecord ban = Main.instance.databaseManager.getBan(id);
        if (ban == null) {
            Debugger.info("Null ban");
            return;
        }

        Main.instance.bot.retrieveUserById(ban.user).queue(user ->
                Main.instance.bot.retrieveUserById(ban.bannedBy).queue(bannedBy ->
                        sendMessage(context, Main.instance.jdaConfig.unbanDetails
                                .parse("name", user.getName())
                                .parse("id", user.getId())
                                .parse("banned_by_name", bannedBy.getName())
                                .parse("banned_by_id", bannedBy.getId())
                                .parse("reason", ban.reason))));
    }

    @Override
    public void executePrivate(PrivateCommandContext commandContext) {
        //Impossible
    }

    @Override
    public boolean isMemberSafe() {
        return false;
    }
}
