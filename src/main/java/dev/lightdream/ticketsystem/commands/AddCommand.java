package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.CommandArgument;
import dev.lightdream.jdaextension.dto.CommandContext;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.dto.Ticket;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.Collections;

public class AddCommand extends DiscordCommand {
    public AddCommand() {
        super(Main.instance, "add", Main.instance.lang.addCommandDescription, Permission.MANAGE_CHANNEL, false, Collections.singletonList(
                new CommandArgument(OptionType.STRING, "user_id", Main.instance.lang.userIDDescription, true)
        ));
    }

    @Override
    public void executeGuild(CommandContext context) {
        Ticket ticket = Main.instance.databaseManager.getTicket(context.getTextChannel().getIdLong());

        if (ticket == null) {
            sendMessage(context, Main.instance.jdaConfig.notTicket);
            return;
        }

        long id;
        try {
            id = Long.parseLong(context.getArgument("user_id").getAsString());
        } catch (Exception e) {
            sendMessage(context, Main.instance.jdaConfig.invalidID);
            return;
        }

        context.getGuild().retrieveMemberById(id).queue(member -> {
            context.getTextChannel().putPermissionOverride(member).setAllow(
                    Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY,
                    Permission.MESSAGE_ATTACH_FILES, Permission.VIEW_CHANNEL
            ).queue();

            sendMessage(context, Main.instance.jdaConfig.addedToTicket
                    .parse("name", member.getEffectiveName()));

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
