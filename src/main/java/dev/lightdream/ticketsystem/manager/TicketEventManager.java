package dev.lightdream.ticketsystem.manager;

import dev.lightdream.logger.Debugger;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.annotation.EventHandler;
import dev.lightdream.ticketsystem.event.TicketCreateEvent;

@SuppressWarnings("unused")
public class TicketEventManager {

    public TicketEventManager() {
        Main.instance.eventManager.register(this);
    }

    @EventHandler
    public void onTicketCreate(TicketCreateEvent event) {
        Debugger.log("Creating ticket: " + event);
    }

}
