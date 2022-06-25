package dev.lightdream.ticketsystem.event;

import dev.lightdream.jdaextension.dto.JDAEmbed;
import dev.lightdream.ticketsystem.dto.TicketType;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class TicketCreateEvent extends TicketEvent {

    private final IReplyCallback replyCallback;
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
        this.guild = guild;
        this.member = member;
        this.type = type;
        this.replyCallback = replyCallback;
    }

    public void reply(JDAEmbed reply) {
        replyCallback.replyEmbeds(reply.build().build()).setEphemeral(true).queue();
    }

}
