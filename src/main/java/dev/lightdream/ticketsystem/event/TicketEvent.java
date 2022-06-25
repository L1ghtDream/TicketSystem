package dev.lightdream.ticketsystem.event;

import dev.lightdream.ticketsystem.Main;

public class TicketEvent {

    private boolean cancelled = false;

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        cancelled = true;
    }

    public void fire() {
        Main.instance.eventManager.fire(this);
    }

}
