package dev.lightdream.ticketsystem.manager;

import dev.lightdream.logger.Debugger;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.annotation.EventHandler;
import dev.lightdream.ticketsystem.database.BlacklistRecord;
import dev.lightdream.ticketsystem.database.Ticket;
import dev.lightdream.ticketsystem.dto.TicketType;
import dev.lightdream.ticketsystem.event.TicketCreateEvent;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import sun.awt.image.OffScreenImageSource;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class TicketEventManager {

    public TicketEventManager() {
        Main.instance.eventManager.register(this);
    }

    @SneakyThrows
    @EventHandler
    public void _onTicketCreate(TicketCreateEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        TicketType ticketType = event.getType();

        if (guild == null) {
            return;
        }

        Category category = guild.getCategoryById(ticketType.categoryID);

        if (category == null || member == null) {
            return;
        }

        BlacklistRecord blacklistRecord = Main.instance.databaseManager.getBlacklist(member.getIdLong());

        if (blacklistRecord != null) {
            event.reply(Main.instance.jdaConfig.blacklisted);
            return;
        }

        for (TextChannel channel : category.getTextChannels()) {
            Ticket ticket = Main.instance.databaseManager.getTicket(channel.getIdLong());

            if (ticket == null) {
                continue;
            }

            if (!ticket.creatorID.equals(member.getIdLong())) {
                continue;
            }

            channel.upsertPermissionOverride(member).setAllowed(
                    Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY,
                    Permission.MESSAGE_ATTACH_FILES, Permission.VIEW_CHANNEL
            ).queue();

            channel.sendMessage("<@" + member.getId() + ">")
                    .queue(message -> message.delete().queue());

            event.reply(Main.instance.jdaConfig.alreadyHaveTicket);
            return;
        }

        CompletableFuture<TextChannel>textChannelCF =  guild.createTextChannel(member.getEffectiveName(), category).submit();

        TextChannel textChannel = textChannelCF.get();
        textChannel.upsertPermissionOverride(member).setAllowed(
                    Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY,
                    Permission.MESSAGE_ATTACH_FILES, Permission.VIEW_CHANNEL
            ).queue();

            ticketType.associatedRanks.forEach(rank -> {
                if (Main.instance.bot.getRoleById(rank) == null) {
                    return;
                }
                //noinspection ConstantConditions
                textChannel.upsertPermissionOverride(Main.instance.bot.getRoleById(rank)).setAllowed(
                        Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY,
                        Permission.MESSAGE_ATTACH_FILES, Permission.VIEW_CHANNEL
                ).queue();
            });

            textChannel.sendMessage("<@" + member.getId() + ">").queue(message ->
                    message.delete().queue());

            new Ticket(ticketType.id, textChannel.getIdLong(), member.getIdLong()).save();

            event.reply(Main.instance.jdaConfig.ticketCreated);
            event.setTextChannel(textChannel);
    }

}
