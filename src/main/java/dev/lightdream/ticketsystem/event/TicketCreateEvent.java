package dev.lightdream.ticketsystem.event;

import dev.lightdream.ticketsystem.dto.TicketType;
import dev.lightdream.ticketsystem.event.generic.InteractionTicketEvent;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class TicketCreateEvent extends InteractionTicketEvent {

    @Getter
    private final Guild guild;
    @Getter
    private final Member member;
    @Getter
    private final TicketType type;
    @Getter
    @Setter
    private TextChannel textChannel;

    public TicketCreateEvent(Guild guild, Member member, TicketType type, IReplyCallback replyCallback) {
        super(replyCallback);
        this.guild = guild;
        this.member = member;
        this.type = type;
    }

}
