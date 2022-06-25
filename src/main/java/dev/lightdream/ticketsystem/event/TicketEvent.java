package dev.lightdream.ticketsystem.event;

import dev.lightdream.ticketsystem.Main;

public class TicketEvent {

    public void fire(){
        Main.instance.eventManager.fire(this);
    }

}
