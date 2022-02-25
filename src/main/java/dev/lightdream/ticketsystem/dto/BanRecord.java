package dev.lightdream.ticketsystem.dto;

import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.databasemanager.dto.DatabaseEntry;
import dev.lightdream.logger.Debugger;
import dev.lightdream.logger.Logger;
import dev.lightdream.ticketsystem.Main;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

@DatabaseTable(table = "bans")
public class BanRecord extends DatabaseEntry {

    @DatabaseField(columnName = "user_id")
    public Long user;
    @DatabaseField(columnName = "ranks")
    public List<Long> ranks;
    @DatabaseField(columnName = "reason")
    public String reason;
    @DatabaseField(columnName = "timestamp")
    public Long timestamp;
    @DatabaseField(columnName = "active")
    public boolean active;

    public BanRecord(Long user, List<Long> ranks, String reason, Long timestamp) {
        super(Main.instance);
        this.user = user;
        this.ranks = ranks;
        this.reason = reason;
        this.timestamp = timestamp;
        this.active = true;
    }

    public BanRecord() {
        super(Main.instance);
    }

    public boolean unban(TextChannel textChannel) {
        if (!active) {
            return false;
        }

        Guild guild = textChannel.getGuild();

        guild.retrieveMemberById(user)
                .queue(member -> {
                    Role bannedRole = guild.getRoleById(Main.instance.config.bannedRank);

                    if (bannedRole != null) {
                        guild.removeRoleFromMember(member, bannedRole)
                                .queue();
                    } else {
                        Logger.error("The banned role is not valid");
                    }

                    ranks.forEach(rank -> {
                        Role role = guild.getRoleById(rank);

                        if (role == null) {
                            Logger.error("Missing role " + role);
                            return;
                        }

                        guild.addRoleToMember(member, role).queue();
                    });

                    textChannel.sendMessage("Restored ranks").queue();
                });

        active = false;
        save();
        return true;
    }
}
