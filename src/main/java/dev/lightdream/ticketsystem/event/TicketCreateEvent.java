package dev.lightdream.ticketsystem.event;

import dev.lightdream.ticketsystem.dto.TicketType;

public class TicketCreateEvent {

    public long channelID;
    public TicketType type;

    public TicketCreateEvent(long channelID, TicketType type) {
        this.channelID = channelID;
        this.type = type;
    }

}
