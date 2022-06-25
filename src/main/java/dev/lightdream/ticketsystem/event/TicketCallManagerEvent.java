package dev.lightdream.ticketsystem.event;

import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.database.Ticket;
import dev.lightdream.ticketsystem.event.generic.InteractionTicketEvent;
import lombok.Getter;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class TicketCallManagerEvent extends InteractionTicketEvent {

    @Getter
    private final MessageChannel channel;

    public TicketCallManagerEvent(MessageChannel channel, IReplyCallback replyCallback) {
        super(replyCallback);
        this.channel = channel;
    }

    public Ticket getTicket() {
        return Main.instance.databaseManager.getTicket(channel.getIdLong());
    }

}
