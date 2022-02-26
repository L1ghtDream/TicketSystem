package dev.lightdream.ticketsystem.manager;

import dev.lightdream.logger.Debugger;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.dto.BanRecord;
import dev.lightdream.ticketsystem.dto.Ticket;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordEventManager extends ListenerAdapter {

    public DiscordEventManager(Main plugin) {
        plugin.bot.addEventListener(this);
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        String id = event.getComponentId();
        Debugger.info(id);

        Conditions:
        if (id.equalsIgnoreCase("close-ticket")) {
            TicketManager.closeTicket(event.getTextChannel());
        } else if (id.equalsIgnoreCase("manager")) {
            MessageChannel channel = event.getChannel();

            Ticket ticket = Main.instance.databaseManager.getTicket(channel.getIdLong());
            if (ticket == null) {
                break Conditions;
            }

            if (ticket.pingedManager) {
                channel.sendMessageEmbeds(Main.instance.jdaConfig.alreadyPingedManager.build().build()).queue();
                break Conditions;
            }

            channel.sendMessage("<@&" + Main.instance.config.managerPingRank + ">").queue(message ->
                    message.delete().queue());

            ticket.pingedManager = true;
            ticket.save();
        } else if (id.equals(Main.instance.config.unbanTicket.id)) {
            User user = event.getUser();

            BanRecord ban = Main.instance.databaseManager.getBan(user.getIdLong());

            if (ban == null) {
                //todo possibly private chanel message
                break Conditions;
            }

            TicketManager.createTicket(event.getGuild(), event.getMember(), Main.instance.config.unbanTicket, textChannel -> {
                String avatar = user.getAvatarUrl() == null ?
                        "https://external-preview.redd.it/4PE-nlL_PdMD5PrFNLnjurHQ1QKPnCvg368LTDnfM-M.png?auto=webp&s=ff4c3fbc1cce1a1856cff36b5d2a40a6d02cc1c3" :
                        user.getAvatarUrl();

                Main.instance.jdaConfig.unbanTicketGreeting
                        .parse("name", user.getName())
                        .parse("avatar", avatar)
                        .buildMessageAction((MessageChannel) textChannel).queue();

                Main.instance.bot.retrieveUserById(ban.bannedBy).queue(bannedBy ->
                        ((MessageChannel) textChannel).sendMessageEmbeds(Main.instance.jdaConfig.unbanDetails
                                .parse("name", user.getName())
                                .parse("id", user.getId())
                                .parse("banned_by_name", bannedBy.getName())
                                .parse("banned_by_id", String.valueOf(bannedBy))
                                .parse("reason", ban.reason)
                                .build().build()).queue());

                ((MessageChannel) textChannel).sendMessage("<@" + ban.bannedBy + ">").queue(message ->
                        message.delete().queue());


                return null;
            });
        } else if (id.equalsIgnoreCase("unban")) {
            Member member = event.getMember();
            TextChannel channel = event.getTextChannel();

            if (member == null || !member.hasPermission(Permission.BAN_MEMBERS)) {
                channel.sendMessageEmbeds(Main.instance.jdaConfig.notAllowed.build().build()).queue();
                break Conditions;
            }

            Ticket ticket = Main.instance.databaseManager.getTicket(channel.getIdLong());

            if (ticket == null) {
                channel.sendMessageEmbeds(Main.instance.jdaConfig.error.build().build()).queue();
                return;
            }

            BanManager.unban(ticket.creatorID, channel);
        } else {

            Main.instance.config.ticketTypes.forEach(ticketType -> {
                if (!ticketType.id.equals(id)) {
                    return;
                }

                TicketManager.createTicket(event.getGuild(), event.getMember(), ticketType, textChannel -> {
                    User user = event.getUser();

                    String avatar = user.getAvatarUrl() == null ?
                            "https://external-preview.redd.it/4PE-nlL_PdMD5PrFNLnjurHQ1QKPnCvg368LTDnfM-M.png?auto=webp&s=ff4c3fbc1cce1a1856cff36b5d2a40a6d02cc1c3" :
                            user.getAvatarUrl();

                    Main.instance.jdaConfig.ticketGreeting
                            .parse("name", user.getName())
                            .parse("avatar", avatar)
                            .buildMessageAction((MessageChannel) textChannel).queue();
                    return null;
                });
            });
        }

        event.deferEdit().queue();

    }


}