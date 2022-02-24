package dev.lightdream.ticketsystem.manager;

import dev.lightdream.logger.Debugger;
import dev.lightdream.ticketsystem.Main;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordEventManager extends ListenerAdapter {

    private final Main main;

    public DiscordEventManager(Main plugin) {
        this.main = plugin;
        plugin.bot.addEventListener(this);
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        Debugger.info(event.getComponentId());

        String id = event.getComponentId();
        Main.instance.config.ticketTypes.forEach(ticketType -> {
            if (!ticketType.id.equals(id)) {
                return;
            }

            if (Main.instance.bot.getCategoryById(ticketType.categoryID) == null) {
                return;
            }

            //noinspection ConstantConditions
            for (GuildChannel channel : Main.instance.bot.getCategoryById(ticketType.categoryID).getChannels()) {
                if (channel.getName().equalsIgnoreCase(event.getUser().getName())) {
                    if (Main.instance.bot.getTextChannelById(channel.getId()) == null) {
                        return;
                    }
                    //noinspection ConstantConditions
                    Main.instance.bot.getTextChannelById(channel.getId()).sendMessage("<@" + event.getUser().getId() + ">").queue(message ->
                            message.delete().queue());
                    return;
                }
            }
            if (event.getGuild() == null) {
                return;
            }

            event.getGuild().createTextChannel(event.getUser().getName(),
                    Main.instance.bot.getCategoryById(ticketType.categoryID)).queue(textChannel -> {
                if (event.getMember() == null) {
                    return;
                }
                textChannel.putPermissionOverride(event.getMember()).setAllow(
                        Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY,
                        Permission.MESSAGE_ATTACH_FILES, Permission.VIEW_CHANNEL
                ).queue();

                ticketType.associatedRanks.forEach(rank -> {
                    if (Main.instance.bot.getRoleById(rank) == null) {
                        return;
                    }
                    //noinspection ConstantConditions
                    textChannel.putPermissionOverride(Main.instance.bot.getRoleById(rank)).setAllow(
                            Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY,
                            Permission.MESSAGE_ATTACH_FILES, Permission.VIEW_CHANNEL
                    ).queue();
                });

                textChannel.sendMessage("<@" + event.getUser().getId() + ">").queue(message ->
                        message.delete().queue());
            });
        });

        event.deferEdit().queue();

    }
}