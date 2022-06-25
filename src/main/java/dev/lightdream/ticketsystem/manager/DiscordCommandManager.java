package dev.lightdream.ticketsystem.manager;

import dev.lightdream.jdaextension.JDAExtensionMain;
import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.logger.Debugger;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DiscordCommandManager extends dev.lightdream.jdaextension.managers.DiscordCommandManager {
    public DiscordCommandManager(JDAExtensionMain main, List<DiscordCommand> commands) {
        super(main, commands);
        Debugger.log("Calling constructor on DiscordCommandManager");
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Debugger.log("Received command event: " + event);
        super.onSlashCommandInteraction(event);
    }
}
