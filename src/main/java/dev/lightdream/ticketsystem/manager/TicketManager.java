package dev.lightdream.ticketsystem.manager;

import dev.lightdream.jdaextension.dto.JDAEmbed;
import dev.lightdream.lambda.LambdaExecutor;
import dev.lightdream.logger.Debugger;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.database.BlacklistRecord;
import dev.lightdream.ticketsystem.database.Ticket;
import dev.lightdream.ticketsystem.dto.TicketType;
import dev.lightdream.ticketsystem.event.TicketCreateEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;
import org.jetbrains.annotations.NotNull;

public class TicketManager {

    public static JDAEmbed closeTicket(TextChannel textChannel, @NotNull User user) {
        Ticket ticket = Main.instance.databaseManager.getTicket(textChannel.getIdLong());

        if (ticket == null) {
            return Main.instance.jdaConfig.notTicket;
        }

        return _closeTicket(textChannel, ticket, user);
    }

    private static JDAEmbed _closeTicket(TextChannel textChannel, @NotNull Ticket ticket, @NotNull User user) {
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
        return Main.instance.jdaConfig.closingTicket;
    }

}
