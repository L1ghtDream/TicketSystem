package dev.lightdream.ticketsystem.event;

import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.database.Ticket;
import dev.lightdream.ticketsystem.event.generic.InteractionTicketEvent;
import lombok.Getter;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class TicketCloseEvent extends InteractionTicketEvent {

    @Getter
    private final TextChannel textChannel;
    @Getter
    private final User user;

    public TicketCloseEvent(TextChannel textChannel, User user, IReplyCallback replyCallback) {
        super(replyCallback);
        this.textChannel = textChannel;
        this.user = user;
    }

    public Ticket getTicket() {
        return Main.instance.databaseManager.getTicket(textChannel.getIdLong());
    }

}
