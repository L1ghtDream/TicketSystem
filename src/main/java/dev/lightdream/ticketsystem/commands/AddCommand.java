package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.dto.Ticket;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.util.List;

public class AddCommand extends DiscordCommand {
    public AddCommand() {
        super(Main.instance, "add", "Adds user to ticket", Permission.MANAGE_CHANNEL, "[user_id]", true);
    }

    @Override
    public void execute(Member sender, TextChannel textChannel, List<String> args) {
        if (args.size() != 1) {
            sendUsage(textChannel);
            return;
        }

        Ticket ticket = Main.instance.databaseManager.getTicket(textChannel.getIdLong());

        if (ticket == null) {
            textChannel.sendMessageEmbeds(Main.instance.jdaConfig.notTicket.build().build()).queue();
            return;
        }

        long id;
        try {
            id = Long.parseLong(args.get(0));
        } catch (Exception e) {
            textChannel.sendMessageEmbeds(Main.instance.jdaConfig.invalidID.build().build()).queue();
            return;
        }

        Guild guild = textChannel.getGuild();

        guild.retrieveMemberById(id).queue(member -> {
            textChannel.putPermissionOverride(member).setAllow(
                    Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY,
                    Permission.MESSAGE_ATTACH_FILES, Permission.VIEW_CHANNEL
            ).queue();

            textChannel.sendMessageEmbeds(Main.instance.jdaConfig.addedToTicket
                    .parse("name", member.getEffectiveName()).build().build()).queue();

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
