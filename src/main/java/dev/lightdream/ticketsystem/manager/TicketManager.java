package dev.lightdream.ticketsystem.manager;

import dev.lightdream.databasemanager.dto.LambdaExecutor;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.dto.Ticket;
import dev.lightdream.ticketsystem.dto.TicketType;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;
import org.jetbrains.annotations.NotNull;

public class TicketManager {

    public static void closeTicket(TextChannel textChannel) {
        Ticket ticket = Main.instance.databaseManager.getTicket(textChannel.getIdLong());

        if (ticket == null) {
            textChannel.sendMessageEmbeds(Main.instance.jdaConfig.notTicket.build().build()).queue();
            return;
        }

        _closeTicket(textChannel, ticket);

        //AtomicBoolean found = new AtomicBoolean(false);

        //if (Main.instance.config.unbanTicket.categoryID.equals(textChannel.getParentCategoryIdLong())) {
        //    _closeTicket(textChannel);
        //    found.set(true);
        //    return;
        //}
        //Main.instance.config.ticketTypes.forEach(ticketType -> {
        //    if (ticketType.categoryID.equals(textChannel.getParentCategoryIdLong())) {
        //        _closeTicket(textChannel);
        //        found.set(true);
        //    }
        //});

        //if (!found.get()) {
        //    textChannel.sendMessageEmbeds(Main.instance.jdaConfig.notTicket.build().build()).queue();
        //}
    }

    private static void _closeTicket(TextChannel textChannel, @NotNull Ticket ticket) {
        textChannel.sendMessageEmbeds(Main.instance.jdaConfig.closingTicket.build().build()).queue();
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                ticket.delete();
                textChannel.delete().queue(null, new ErrorHandler().handle(
                        ErrorResponse.UNKNOWN_CHANNEL,
                        e -> {
                            //empty
                        }
                ));
            }
        }, 5000);
    }


    public static void createTicket(Guild guild, Member member, TicketType ticketType, LambdaExecutor executeOnChannel) {
        if (guild == null) {
            return;
        }

        Category category = guild.getCategoryById(ticketType.categoryID);

        if (category == null || member == null) {
            return;
        }

        for (GuildChannel channel : category.getChannels()) {
            if (channel.getName().equalsIgnoreCase(member.getEffectiveName())) {
                if (Main.instance.bot.getTextChannelById(channel.getId()) == null) {
                    return;
                }
                //noinspection ConstantConditions
                Main.instance.bot.getTextChannelById(channel.getId()).sendMessage("<@" + member.getId() + ">")
                        .queue(message -> message.delete().queue());
                return;
            }
        }

        guild.createTextChannel(member.getEffectiveName(), category).queue(textChannel -> {
            textChannel.putPermissionOverride(member).setAllow(
                    Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY,
                    Permission.MESSAGE_ATTACH_FILES, Permission.VIEW_CHANNEL
            ).queue();

            ticketType.associatedRanks.forEach(rank -> {
                if (Main.instance.bot.getRoleById(rank) == null) {
                    return;
                }
                //noinspection ConstantConditions
                textChannel.putPermissionOverride(Main.instance.bot.getRoleById(rank)).setAllow(
                        Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY,
                        Permission.MESSAGE_ATTACH_FILES, Permission.VIEW_CHANNEL
                ).queue();
            });

            textChannel.sendMessage("<@" + member.getId() + ">").queue(message ->
                    message.delete().queue());

            new Ticket(ticketType.id, textChannel.getIdLong(), member.getIdLong()).save();

            executeOnChannel.execute(textChannel);
        });
    }

}
