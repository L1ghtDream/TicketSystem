package dev.lightdream.ticketsystem.manager;

import dev.lightdream.logger.Debugger;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.annotation.EventHandler;
import dev.lightdream.ticketsystem.database.BanRecord;
import dev.lightdream.ticketsystem.database.BlacklistRecord;
import dev.lightdream.ticketsystem.database.Ticket;
import dev.lightdream.ticketsystem.dto.TicketType;
import dev.lightdream.ticketsystem.event.*;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class TicketEventManager {

    public TicketEventManager() {
        Main.instance.eventManager.register(this);
    }

    @EventHandler
    public void onTicketCreate(TicketCreateEvent event) {
        if(event.isCancelled()){
            return;
        }

        TicketType ticketType = event.getType();

        Debugger.log(ticketType.handler);

        switch (ticketType.handler) {
            case "unban":
                checkBanTicketRequirements(event);
                if (event.isCancelled()) {
                    return;
                }
                generalTicketSetup(event);
                completeTicketAsBan(event);
                break;
            case "general":
                generalTicketSetup(event);
                completeTicketAsGeneral(event);
                break;
            case "dialogue":
                break;
        }

    }

    @EventHandler
    public void onTicketClose(TicketCloseEvent event) {
        if(event.isCancelled()){
            return;
        }

        TextChannel textChannel = event.getTextChannel();

        Ticket ticket = Main.instance.databaseManager.getTicket(textChannel.getIdLong());

        if (ticket == null) {
            event.reply(Main.instance.jdaConfig.notTicket);
            return;
        }

        closeTicket(event);

        event.getTextChannel().sendMessageEmbeds(Main.instance.jdaConfig.closingTicket.clone().build().build()).queue();
        event.close();
    }

    @EventHandler
    public void onManagerCall(TicketCallManagerEvent event){
        if(event.isCancelled()){
            return;
        }

        MessageChannel channel = event.getChannel();

        Ticket ticket = Main.instance.databaseManager.getTicket(channel.getIdLong());
        if (ticket == null) {
            return;
        }

        event.close();

        if (ticket.pingedManager) {
            channel.sendMessageEmbeds(Main.instance.jdaConfig.alreadyPingedManager.build().build()).queue();
            return;
        }

        channel.sendMessage("<@&" + Main.instance.config.managerPingRank + ">").queue(message ->
                message.delete().queue());

        ticket.setPingedManager(true);
    }

    @EventHandler
    public void onTicketUnbanEvent(TicketUnbanEvent event){
        if(event.isCancelled()){
            return;
        }

        Member member = event.getMember();
        TextChannel channel = event.getTextChannel();
        event.close();

        if (member == null || !member.hasPermission(Permission.BAN_MEMBERS)) {
            event.reply(Main.instance.jdaConfig.notAllowed);
            return;
        }

        Ticket ticket = Main.instance.databaseManager.getTicket(channel.getIdLong());

        if (ticket == null) {
            event.reply(Main.instance.jdaConfig.error);
            return;
        }

        new UnbanEvent(event.getInteraction(), ticket.creatorID).fire();
    }

    @EventHandler
    public void onUnban(UnbanEvent event){
        if(event.isCancelled()){
            return;
        }

        Long bannedID = event.getBannedID();

        BanRecord ban = Main.instance.databaseManager.getBan(bannedID);

        if (ban == null) {
            event.reply(Main.instance.jdaConfig.notBanned);
            return;
        }

        if (!ban.unban(event)) {
            event.reply(Main.instance.jdaConfig.notBanned);
        }
    }


    @SneakyThrows
    private void generalTicketSetup(TicketCreateEvent event) {
        if(event.isCancelled()){
            return;
        }

        Guild guild = event.getGuild();
        Member member = event.getMember();
        TicketType ticketType = event.getType();

        if (guild == null) {
            Debugger.log("[1] #setCanceled");
            event.setCancelled(true);
            return;
        }

        Category category = guild.getCategoryById(ticketType.categoryID);

        if (category == null || member == null) {
            Debugger.log("[2] #setCanceled");
            event.setCancelled(true);
            return;
        }

        BlacklistRecord blacklistRecord = Main.instance.databaseManager.getBlacklist(member.getIdLong());

        if (blacklistRecord != null) {
            event.reply(Main.instance.jdaConfig.blacklisted);
            Debugger.log("[3] #setCanceled");
            event.setCancelled(true);
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
            Debugger.log("[4] #setCanceled");
            event.setCancelled(true);
            return;
        }

        CompletableFuture<TextChannel> textChannelCF = guild.createTextChannel(member.getEffectiveName(), category).submit();

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
        Debugger.log("Setting textChannel to " + textChannel);
        event.setTextChannel(textChannel);
    }

    private void checkBanTicketRequirements(TicketCreateEvent event) {
        if(event.isCancelled()){
            return;
        }

        User user = event.getMember().getUser();
        BanRecord ban = Main.instance.databaseManager.getBan(user.getIdLong());

        if (ban == null) {
            Debugger.log("[5] #setCanceled");
            event.reply(Main.instance.jdaConfig.notBanned);
            event.setCancelled(true);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void completeTicketAsBan(TicketCreateEvent event) {
        if(event.isCancelled()){
            return;
        }

        User user = event.getMember().getUser();
        TextChannel textChannel = event.getTextChannel();
        BanRecord ban = Main.instance.databaseManager.getBan(user.getIdLong());

        String avatar = user.getAvatarUrl();

        if (avatar == null) {
            avatar = "https://external-preview.redd.it/4PE-nlL_PdMD5PrFNLnjurHQ1QKPnCvg368LTDnfM-M.png?auto=webp&s=ff4c3fbc1cce1a1856cff36b5d2a40a6d02cc1c3";
        }

        Debugger.log("TextChannel: " + textChannel);

        Main.instance.jdaConfig.unbanTicketGreeting
                .parse("name", user.getName())
                .parse("avatar", avatar)
                .buildMessageAction(textChannel).queue();

        ban.sendBanDetails(textChannel);

        textChannel.sendMessage("<@" + ban.bannedBy + ">").queue(message -> {
            message.delete().queue();
        });

        if (!ban.isApplicable()) {
            ban.unban(event);
        }
    }

    private void completeTicketAsGeneral(TicketCreateEvent event) {
        if(event.isCancelled()){
            return;
        }

        TextChannel textChannel = event.getTextChannel();

        User user = event.getMember().getUser();

        String avatar = user.getAvatarUrl();
        if (avatar == null) {
            avatar = "https://external-preview.redd.it/4PE-nlL_PdMD5PrFNLnjurHQ1QKPnCvg368LTDnfM-M.png?auto=webp&s=ff4c3fbc1cce1a1856cff36b5d2a40a6d02cc1c3";
        }

        Main.instance.jdaConfig.ticketGreeting
                .parse("name", user.getName())
                .parse("avatar", avatar)
                .buildMessageAction(textChannel).queue();
    }

    private void closeTicket(TicketCloseEvent event) {
        if(event.isCancelled()){
            return;
        }

        Ticket ticket = event.getTicket();
        User user = event.getUser();
        TextChannel textChannel = event.getTextChannel();

        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                ticket.getTranscript().record(user, "Closed Ticket");
                ticket.close();
                textChannel.delete().queue(null, new ErrorHandler().handle(
                        ErrorResponse.UNKNOWN_CHANNEL,
                        e -> {
                            //empty
                        }
                ));
            }
        }, 5000);
    }

    private void dialogueTicketSetup(TicketCreateEvent event){
        if(event.isCancelled()){
            return;
        }
    }


}
