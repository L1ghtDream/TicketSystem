package dev.lightdream.ticketsystem.dto;

import dev.lightdream.jdaextension.dto.JDALang;

public class Lang implements JDALang {

    public String addCommandDescription = "Adds user to ticket";
    public String banCommandDescription = "Bans an user";
    public String banDetailsCommandDescription = "Checks for ban details";
    public String checkBanCommandDescription = "Checks if an user is banned";
    public String closeCommandDescription = "Closes the ticket you are in";
    public String historyCommandDescription = "Shows the history of an user";
    public String setupCommandDescription = "Sets up the messages in the channel";
    public String ticketsCommandDescription = "Shows the tickets of a user";
    public String transcriptCommandDescription = "See the transcript of a ticket";
    public String unbanCommandDescription = "Unbans an user";
    public String helpCommandDescription = "Send the help command";
    public String statsCommandDescription = "Shows technical details about the bot and its environment";
    public String userIDDescription = "User ID";
    public String banReasonDescription = "Ban reason";
    public String banIDDescription = "Ban ID";
    public String ticketIDDescription = "Ticket ID";
    public String blacklistCommandDescription = "Prevent a user from being able to create furthere tickets";
    public String blackListSubCommandsDescription = "[add/remove/check]";
    public String userArgumentDescription = "User upon whom to take the action";


    @Override
    public String getHelpCommandDescription() {
        return helpCommandDescription;
    }

    @Override
    public String getStatsCommandDescription() {
        return statsCommandDescription;
    }
}
