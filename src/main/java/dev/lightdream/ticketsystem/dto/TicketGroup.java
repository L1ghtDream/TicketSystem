package dev.lightdream.ticketsystem.dto;

import dev.lightdream.jdaextension.dto.JDAButton;
import dev.lightdream.jdaextension.dto.JDAEmbed;
import dev.lightdream.jdaextension.enums.JDAButtonType;
import dev.lightdream.logger.Logger;
import dev.lightdream.ticketsystem.Main;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.List;

public class TicketGroup {

    public Long setupChannel;
    public List<TicketType> ticketTypes;
    public JDAEmbed setupMessage;

    public TicketGroup() {
    }

    public TicketGroup(Long setupChannel, List<TicketType> ticketTypes, JDAEmbed setupMessage) {
        this.setupChannel = setupChannel;
        this.ticketTypes = ticketTypes;
        this.setupMessage = setupMessage;
    }

    public void sendSetupMessage() {
        JDAEmbed ticket = setupMessage.clone();
        ticketTypes.forEach(ticketType -> {
            JDAButton button = new JDAButton(JDAButtonType.PRIMARY, ticketType.id, ticketType.name);
            ticket.jdaButtons.add(button);
        });

        MessageChannel channel = Main.instance.bot.getTextChannelById(setupChannel);

        if (channel == null) {
            Logger.error("Could not find channel with id: " + setupChannel);
            return;
        }

        ticket.buildMessageAction(channel).queue();
    }
}
