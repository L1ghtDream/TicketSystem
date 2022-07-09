package dev.lightdream.ticketsystem.database;

import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.databasemanager.dto.entry.impl.IntegerDatabaseEntry;
import dev.lightdream.jdaextension.dto.JDAEmbed;
import dev.lightdream.jdaextension.dto.context.CommandContext;
import dev.lightdream.logger.Logger;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.event.generic.InteractionTicketEvent;
import dev.lightdream.ticketsystem.utils.Utils;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@DatabaseTable(table = "bans")
public class BanRecord extends IntegerDatabaseEntry {

    @DatabaseField(columnName = "user_id")
    public Long user;
    @DatabaseField(columnName = "banned_by")
    public Long bannedBy;
    @DatabaseField(columnName = "ranks")
    public List<Long> ranks;
    @DatabaseField(columnName = "reason")
    public String reason;
    @DatabaseField(columnName = "timestamp")
    public Long timestamp;
    @DatabaseField(columnName = "active")
    public boolean active;
    @DatabaseField(columnName = "duration")
    public Long duration; //0 for permanent

    /**
     * @param user      The user to ban
     * @param bannedBy  The user who banned the user
     * @param ranks     The ranks that the user had before being banned.
     * @param reason    reason
     * @param timestamp JAVA time
     * @param duration  time in ms, 0 for permanent
     */
    public BanRecord(Long user, Long bannedBy, List<Long> ranks, String reason, Long timestamp, Long duration) {
        super(Main.instance);
        this.user = user;
        this.bannedBy = bannedBy;
        this.ranks = ranks;
        this.reason = reason;
        this.timestamp = timestamp;
        this.active = true;
        this.duration = duration;

        Main.security.banUser(user, bannedBy, reason, duration);
    }

    @SuppressWarnings("unused")
    public BanRecord() {
        super(Main.instance);
    }

    @SneakyThrows
    public boolean unban(InteractionTicketEvent event) {
        if (!active) {
            return false;
        }

        Guild guild = event.getGuild();

        if (guild == null) {
            event.reply(Main.instance.jdaConfig.serverCommand);
            return false;
        }

        CompletableFuture<Member> memberCF = guild.retrieveMemberById(user).submit();
        Member member = memberCF.get();
        Role bannedRole = guild.getRoleById(Main.instance.config.bannedRank);

        if (bannedRole != null) {
            guild.removeRoleFromMember(member, bannedRole).queue();
        } else {
            event.reply(Main.instance.jdaConfig.invalidUser);
            return false;
        }

        AtomicInteger roles1 = new AtomicInteger();

        ranks.forEach(rank -> {
            Role role = guild.getRoleById(rank);

            if (role == null) {
                Logger.error("Missing role " + role);
                return;
            }

            try {
                guild.addRoleToMember(member, role).queue(s -> {

                }, e -> {

                });
            } catch (HierarchyException e) {
                event.reply(Main.instance.jdaConfig.cannotUnban);
                return;
            }

            roles1.getAndIncrement();
        });

        event.reply(Main.instance.jdaConfig.unBanned
                .parse("name", member.getEffectiveName())
                .parse("roles_1", String.valueOf(roles1.get()))
                .parse("roles_2", String.valueOf(ranks.size()))
        );

        setActive(false);
        Main.security.unbanUser(user);
        return true;
    }

    public void setActive(boolean active) {
        this.active = active;
        save();
    }

    public boolean isApplicable() {
        if (duration == 0) {
            return true;
        }
        return System.currentTimeMillis() <= timestamp + duration;
    }

    public void sendBanDetails(TextChannel textChannel) {
        Main.instance.bot.retrieveUserById(this.bannedBy).queue(bannedBy ->
                Main.instance.bot.retrieveUserById(this.user).queue(user ->
                        textChannel.sendMessageEmbeds(getJDAEmbedDetails(user, bannedBy).build().build()).queue()
                )
        );
    }

    public void sendBanDetails(CommandContext context, boolean privateMessage) {
        Main.instance.bot.retrieveUserById(this.bannedBy).queue(bannedBy ->
                Main.instance.bot.retrieveUserById(this.user).queue(user ->
                        context.sendMessage(getJDAEmbedDetails(user, bannedBy), privateMessage)
                )
        );
    }

    public JDAEmbed getJDAEmbedDetails(User user, User bannedBy) {
        return Main.instance.jdaConfig.unbanDetails
                .parse("name", user.getName())
                .parse("id", user.getId())
                .parse("banned_by_name", bannedBy.getName())
                .parse("banned_by_id", bannedBy.getId())
                .parse("reason", this.reason)
                .parse("date", Utils.msToDate(this.timestamp))
                .parse("unban_date", this.duration == 0 ? "Permanent" : Utils.msToDate(this.timestamp + this.duration));
    }

}
