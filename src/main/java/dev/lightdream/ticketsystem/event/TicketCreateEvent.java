package dev.lightdream.ticketsystem.event;

import dev.lightdream.jdaextension.dto.JDAEmbed;
import dev.lightdream.ticketsystem.dto.TicketType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class TicketCreateEvent extends TicketEvent {

    private Guild guild;
    private Member member;
    private TicketType type;
    private TextChannel textChannel;
    private final IReplyCallback replyCallback;

    public TicketCreateEvent(Guild guild, Member member, TicketType type, IReplyCallback replyCallback) {
        this.guild = guild;
        this.member = member;
        this.type = type;
        this.replyCallback = replyCallback;
    }

    public void reply(JDAEmbed reply){
        replyCallback.replyEmbeds(reply.build().build()).setEphemeral(true).queue();
    }

    @Override
    public String toString() {
        return "TicketCreateEvent{" +
                "guild=" + guild +
                ", member=" + member +
                ", type=" + type +
                ", textChannel=" + textChannel +
                ", replyCallback=" + replyCallback +
                '}';
    }

    public Guild getGuild() {
        return guild;
    }

    public Member getMember() {
        return member;
    }

    public TicketType getType() {
        return type;
    }

    public TextChannel getTextChannel() {
        return textChannel;
    }

    public void setTextChannel(TextChannel textChannel) {
        this.textChannel = textChannel;
    }
}
