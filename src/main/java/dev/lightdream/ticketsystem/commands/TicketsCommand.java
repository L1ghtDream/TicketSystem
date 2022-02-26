package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.JdaEmbed;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.dto.Ticket;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TicketsCommand extends DiscordCommand {
    public TicketsCommand() {
        super(Main.instance, "tickets", "Shows the tickets of a user", Permission.ADMINISTRATOR, "[user_id]", true);
    }

    @Override
    public void execute(Member sender, TextChannel textChannel, List<String> args) {
        if (args.size() != 1) {
            sendUsage(textChannel);
            return;
        }

        long id;
        try {
            id = Long.parseLong(args.get(0));
        } catch (Exception e) {
            textChannel.sendMessageEmbeds(Main.instance.jdaConfig.invalidID.build().build()).queue();
            return;
        }

        Main.instance.bot.retrieveUserById(id).queue(user -> {
            List<Ticket> tickets = Main.instance.databaseManager.getPastTickets(id);
            JdaEmbed embed = Main.instance.jdaConfig.tickets.clone();
            tickets.forEach(ticket -> {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Date date = new Date(ticket.timestamp);

                embed.description += Main.instance.jdaConfig.ticketsEntry
                        .replace("%id%", String.valueOf(ticket.id))
                        .replace("%type%", ticket.type)
                        .replace("%date%", dateFormat.format(date));
                embed.description += "\n";
            });
            textChannel.sendMessageEmbeds(embed
                    .parse("name", user.getName())
                    .build().build()).queue();
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
