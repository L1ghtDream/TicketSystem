package dev.lightdream.ticketsystem.event.generic;

import dev.lightdream.ticketsystem.Main;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.TextChannel;

public abstract class TicketEvent {

    @Setter
    private boolean cancelled = false;
    @Getter
    @Setter
    private TextChannel textChannel;

    public boolean isCancelled() {
        return cancelled;
    }

    public void fire() {
        Main.instance.eventManager.fire(this);
    }

}
