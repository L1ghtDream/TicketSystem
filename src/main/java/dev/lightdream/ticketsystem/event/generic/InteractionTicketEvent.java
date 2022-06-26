package dev.lightdream.ticketsystem.event.generic;

import dev.lightdream.jdaextension.dto.JDAEmbed;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public abstract class InteractionTicketEvent extends TicketEvent {

    @Getter
    private final IReplyCallback interaction;

    public InteractionTicketEvent(IReplyCallback interaction) {
        this.interaction = interaction;
    }

    public void reply(JDAEmbed reply) {
        if (interaction == null) {
            return;
        }

        if(interaction.isAcknowledged()){
            interaction.getHook().editOriginalEmbeds(reply.build().build()).queue();
            return;
        }

        interaction.replyEmbeds(reply.build().build()).setEphemeral(true).queue();
    }

    public void close() {
        if (interaction == null || interaction.isAcknowledged()) {
            return;
        }
        interaction.deferReply().setEphemeral(true).queue();
    }

    public Guild getGuild() {
        if (interaction == null) {
            return null;
        }
        return interaction.getGuild();
    }

}
