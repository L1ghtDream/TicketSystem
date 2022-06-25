package dev.lightdream.ticketsystem.event;

import dev.lightdream.ticketsystem.database.Ticket;
import dev.lightdream.ticketsystem.dto.TicketType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class TicketCreateEvent extends TicketEvent {

    public Guild guild;
    public Member member;
    public TicketType type;

    public TicketCreateEvent(Guild guild, Member member, TicketType type) {
        this.guild = guild;
        this.member = member;
        this.type = type;
    }

    @Override
    public String toString() {
        return "TicketCreateEvent{" +
                "guild=" + guild +
                ", member=" + member +
                ", type=" + type +
                '}';
    }
}
