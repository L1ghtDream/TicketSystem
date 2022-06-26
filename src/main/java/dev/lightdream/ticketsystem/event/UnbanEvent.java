package dev.lightdream.ticketsystem.event;

import dev.lightdream.ticketsystem.event.generic.InteractionTicketEvent;
import lombok.Getter;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class UnbanEvent extends InteractionTicketEvent {

    @Getter
    private final Long bannedID;

    public UnbanEvent(IReplyCallback replyCallback, Long bannedID) {
        super(replyCallback);
        this.bannedID = bannedID;
    }
}
