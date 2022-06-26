package dev.lightdream.ticketsystem.manager;

import dev.lightdream.logger.Logger;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.database.BanRecord;
import dev.lightdream.ticketsystem.database.Ticket;
import dev.lightdream.ticketsystem.dto.TicketType;
import dev.lightdream.ticketsystem.event.TicketCallManagerEvent;
import dev.lightdream.ticketsystem.event.TicketCloseEvent;
import dev.lightdream.ticketsystem.event.TicketCreateEvent;
import dev.lightdream.ticketsystem.event.TicketUnbanEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DiscordEventManager extends ListenerAdapter {

    public DiscordEventManager(Main plugin) {
        plugin.bot.addEventListener(this);
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String id = event.getComponentId();

        if (id.equalsIgnoreCase("close-ticket")) {
            new TicketCloseEvent(event.getTextChannel(), event.getUser(), event).fire();
            return;
        }
        if (id.equalsIgnoreCase("manager")) {
            new TicketCallManagerEvent(event.getChannel(), event).fire();
            return;
        }
        if (id.equalsIgnoreCase("unban")) {
            new TicketUnbanEvent(event, event.getMember()).fire();
            return;
        }

        TicketType type = Main.instance.config.getTicketTypeByID(id);

        if (type == null) {
            return;
        }

        new TicketCreateEvent(event.getGuild(), event.getMember(), type, event).fire();

    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || !event.isFromGuild()) {
            return;
        }

        try {
            TicketType ticketType = Main.instance.config.getTicketTypeByCategoryID(event.getTextChannel().getParentCategoryIdLong());

            if (ticketType == null) {
                return;
            }

            Ticket ticket = Main.instance.databaseManager.getTicket(event.getChannel().getIdLong());

            if (ticket == null) {
                return;
            }

            ticket.getTranscript().record(event.getAuthor(), event.getMessage().getContentRaw());
        } catch (Throwable t) {
            //Empty
        }
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

        BanRecord banRecord = Main.instance.databaseManager.getBan(event.getMember().getIdLong());
        if (banRecord == null) {
            return;
        }

        Member member = event.getMember();
        Guild guild = event.getGuild();

        List<Long> ranks = new ArrayList<>();

        member.getRoles().forEach(role -> ranks.add(role.getIdLong()));

        for (Long rank : ranks) {
            Role role = guild.getRoleById(rank);
            if (role == null) {
                continue;
            }
            try {
                guild.removeRoleFromMember(member, role).queue();
            } catch (HierarchyException e) {
                Logger.error(Main.instance.jdaConfig.cannotBan.description);
                return;
            }
        }

        Role role = guild.getRoleById(Main.instance.config.bannedRank);

        if (role == null) {
            Logger.error(Main.instance.jdaConfig.invalidBannedRole.description);
            return;
        }

        guild.addRoleToMember(member, role).queue();
    }
}


