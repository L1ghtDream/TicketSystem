package dev.lightdream.ticketsystem.event;

import lombok.Getter;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class TicketCloseEvent extends TicketEvent {

    @Getter
    private final TextChannel textChannel;
    @Getter
    private final User user;
    private final IReplyCallback replyCallback;

    public TicketCloseEvent(TextChannel textChannel, User user,IReplyCallback replyCallback) {
        this.textChannel = textChannel;
        this.user = user;
        this.replyCallback = replyCallback;
    }

    public void close(){
        replyCallback.deferReply().queue();
    }

}
