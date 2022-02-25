package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.JDAExtensionMain;
import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.dto.BanRecord;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.util.ArrayList;
import java.util.List;

public class BanCommand extends DiscordCommand {
    public BanCommand(JDAExtensionMain main) {
        super(main, "ban", "Bans user", Permission.BAN_MEMBERS, "[user_id] [reason]",true);
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
            args.subList(1, args.size() - 1)
                    .forEach(word -> reason.append(word)
                            .append(" "));
        } catch (Exception e) {
            textChannel.sendMessage(Main.instance.lang.invalidID).queue();
            return;
        }

        if(Main.instance.databaseManager.getBan(id) != null){
            textChannel.sendMessage("User is already banned").queue();
            return;
        }


        List<Long> ranks = new ArrayList<>();

        textChannel.getGuild()
                .retrieveMemberById(id)
                .queue(member -> {
                    if (member == null) {
                        textChannel.sendMessage("This is not a valid user")
                                .queue(); //todo
                        return;
                    }

                    Guild guild = textChannel.getGuild();

                    member.getRoles()
                            .forEach(role -> ranks.add(role.getIdLong()));

                    ranks.forEach(rank -> {
                        Role role = textChannel.getGuild()
                                .getRoleById(rank);
                        if (role == null) {
                            return;
                        }
                        guild.removeRoleFromMember(member, role)
                                .queue();
                    });

                    Role role = guild.getRoleById(Main.instance.config.bannedRank);

                    if (role == null) {
                        textChannel.sendMessage("The banned role is invalid please check it")
                                .queue();
                        return;
                    }

                    guild.addRoleToMember(member, role)
                            .queue();

                    new BanRecord(id, ranks, reason.toString(), System.currentTimeMillis()).save();
                    textChannel.sendMessage("User banned")
                            .queue(); //todo
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
