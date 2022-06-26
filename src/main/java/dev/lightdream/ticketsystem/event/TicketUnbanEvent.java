package dev.lightdream.ticketsystem.event;

import dev.lightdream.ticketsystem.event.generic.InteractionTicketEvent;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;


public class TicketUnbanEvent extends InteractionTicketEvent {

    /**
     * This is the membered that triggered the event to fire
     */
    @Getter
    private final Member member;

    public TicketUnbanEvent(IReplyCallback replyCallback, Member member, TextChannel textChannel) {
        super(replyCallback);
        this.member = member;
        this.setTextChannel(textChannel);
    }
}
