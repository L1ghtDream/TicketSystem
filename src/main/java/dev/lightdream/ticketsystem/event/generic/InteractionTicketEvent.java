package dev.lightdream.ticketsystem.event.generic;

import dev.lightdream.jdaextension.dto.JDAEmbed;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public abstract class InteractionTicketEvent extends TicketEvent {

    private final IReplyCallback replyCallback;

    public InteractionTicketEvent(IReplyCallback replyCallback) {
        this.replyCallback = replyCallback;
    }

    public void reply(JDAEmbed reply) {
        if (replyCallback == null) {
            return;
        }
        replyCallback.replyEmbeds(reply.build().build()).setEphemeral(true).queue();
    }

    public void close() {
        if (replyCallback == null || replyCallback.isAcknowledged()){
            return;
        }
        replyCallback.deferReply().setEphemeral(true).queue();
    }

}
