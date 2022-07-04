package dev.lightdream.ticketsystem.manager;

import dev.lightdream.ticketsystem.database.Ticket;
import net.dv8tion.jda.api.entities.Message;

public class TicketDialogueManager {

    public TicketDialogueManager() {

    }

    public void onDialogueMessageReceive(Ticket ticket, Message message) {
        if (ticket.isDialogueEnded()) {
            return;
        }

        message.delete().queue();

        if (!ticket.creatorID.equals(message.getAuthor().getIdLong())) {
            return;
        }

        ticket.registerResponse(message.getContentRaw());
    }

}
