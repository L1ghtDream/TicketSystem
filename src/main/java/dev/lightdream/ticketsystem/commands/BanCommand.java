package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.dto.BanRecord;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.util.ArrayList;
import java.util.List;

public class BanCommand extends DiscordCommand {
    public BanCommand() {
        super(Main.instance, "ban", "", Permission.BAN_MEMBERS, "[user_id] [reason]", true);
    }

    @Override
    public void execute(Member sender, TextChannel textChannel, List<String> args) {
        if (args.size() < 2) {
            sendUsage(textChannel);
            return;
        }
        long id;
        StringBuilder reason = new StringBuilder();
        try {
            id = Long.parseLong(args.get(0));
            args.subList(1, args.size())
                    .forEach(word -> reason.append(word)
                            .append(" "));
        } catch (Exception e) {
            textChannel.sendMessageEmbeds(Main.instance.jdaConfig.invalidID.build().build()).queue();
            return;
        }

        if (Main.instance.databaseManager.getBan(id) != null) {
            textChannel.sendMessageEmbeds(Main.instance.jdaConfig.alreadyBanned.build().build()).queue();
            return;
        }


        List<Long> ranks = new ArrayList<>();

        textChannel.getGuild()
                .retrieveMemberById(id)
                .queue(member -> {
                    if (member == null) {
                        textChannel.sendMessageEmbeds(Main.instance.jdaConfig.invalidUser.build().build()).queue();
                        return;
                    }

                    Guild guild = textChannel.getGuild();

                    member.getRoles().forEach(role -> ranks.add(role.getIdLong()));

                    for (Long rank : ranks) {
                        Role role = textChannel.getGuild().getRoleById(rank);
                        if (role == null) {
                            continue;
                        }
                        try {
                            guild.removeRoleFromMember(member, role).queue();
                        } catch (HierarchyException e) {
                            textChannel.sendMessageEmbeds(Main.instance.jdaConfig.cannotBan.build().build()).queue();
                            return;
                        }
                    }

                    Role role = guild.getRoleById(Main.instance.config.bannedRank);

                    if (role == null) {
                        textChannel.sendMessageEmbeds(Main.instance.jdaConfig.invalidBannedRole.build().build()).queue();
                        return;
                    }

                    guild.addRoleToMember(member, role).queue();

                    new BanRecord(id, sender.getIdLong(), ranks, reason.toString(), System.currentTimeMillis()).save();
                    textChannel.sendMessageEmbeds(Main.instance.jdaConfig.userBanned
                                    .parse("name", member.getEffectiveName()).build().build())
                            .queue();
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
