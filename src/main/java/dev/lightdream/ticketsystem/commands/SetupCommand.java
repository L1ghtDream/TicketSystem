package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.JDAExtensionMain;
import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.Button;
import dev.lightdream.jdaextension.dto.JdaEmbed;
import dev.lightdream.jdaextension.enums.JDAButtonType;
import dev.lightdream.ticketsystem.Main;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class SetupCommand extends DiscordCommand {

    public SetupCommand(JDAExtensionMain main) {
        super(main, "setup", "Sends the necessary messages", Permission.ADMINISTRATOR, "", true);
    }

    @Override
    public void execute(Member member, TextChannel textChannel, List<String> list) {
        JdaEmbed embed = Main.instance.jdaConfig.ticket.clone();
        Main.instance.config.ticketTypes.forEach(ticketType ->
                embed.buttons.add(new Button(JDAButtonType.PRIMARY, ticketType.id, ticketType.name)));
        embed.buildMessageAction(Main.instance.bot.getTextChannelById(Main.instance.config.ticketsChanel)).queue();

        textChannel.sendMessage("Setup finished").queue();
    }

    @Override
    public void execute(User user, MessageChannel messageChannel, List<String> list) {
        //Imposible
    }

    @Override
    public boolean isMemberSafe() {
        return false;
    }
}
