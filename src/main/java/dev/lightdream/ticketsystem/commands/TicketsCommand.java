package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.CommandArgument;
import dev.lightdream.jdaextension.dto.CommandContext;
import dev.lightdream.jdaextension.dto.JdaEmbed;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.dto.Ticket;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TicketsCommand extends DiscordCommand {
    public TicketsCommand() {
        super(Main.instance, "tickets", Main.instance.lang.ticketsCommandDescription, Permission.ADMINISTRATOR, true, Collections.singletonList(
                new CommandArgument(OptionType.STRING, "user_id", Main.instance.lang.userIDDescription, true)
        ));
    }

    @Override
    public void executeGuild(CommandContext context) {
        long id;
        try {
            id = Long.parseLong(context.getArgument("user_id").getAsString());
        } catch (Exception e) {
            sendMessage(context, Main.instance.jdaConfig.invalidID);
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

            sendMessage(context, embed
                    .parse("name", user.getName()));
        });


    }

    @Override
    public void executePrivate(CommandContext commandContext) {

    }

    @Override
    public boolean isMemberSafe() {
        return false;
    }
}
