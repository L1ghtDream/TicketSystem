package dev.lightdream.ticketsystem.event;

import dev.lightdream.ticketsystem.Main;
import lombok.Setter;

public class TicketEvent {

    @Setter
    private boolean cancelled = false;

    public boolean isCancelled() {
        return cancelled;
    }

    public void fire() {
        Main.instance.eventManager.fire(this);
    }

}
