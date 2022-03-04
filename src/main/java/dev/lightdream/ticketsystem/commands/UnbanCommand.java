package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.CommandArgument;
import dev.lightdream.jdaextension.dto.CommandContext;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.manager.BanManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.Collections;

public class UnbanCommand extends DiscordCommand {
    public UnbanCommand() {
        super(Main.instance, "unban", Main.instance.lang.unbanCommandDescription, Permission.BAN_MEMBERS, true, Collections.singletonList(
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

        BanManager.unban(id, context.getTextChannel());
    }

    @Override
    public void executePrivate(CommandContext commandContext) {

    }

    @Override
    public boolean isMemberSafe() {
        return false;
    }
}
