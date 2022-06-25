package dev.lightdream.ticketsystem.manager;

import dev.lightdream.logger.Debugger;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.annotation.EventListener;
import dev.lightdream.ticketsystem.event.TicketCreateEvent;

public class TicketEventManager {

    public TicketEventManager() {
        Main.instance.eventManager.register(this);
    }

    @EventListener
    public void onTicketCreate(TicketCreateEvent event) {
        Debugger.log("Creating ticket");
    }

}
