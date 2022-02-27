package dev.lightdream.ticketsystem.dto;

import dev.lightdream.jdaextension.dto.Button;
import dev.lightdream.jdaextension.dto.JdaEmbed;
import dev.lightdream.jdaextension.dto.JdaField;
import dev.lightdream.jdaextension.enums.JDAButtonType;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class JDAConfig extends dev.lightdream.jdaextension.dto.JDAConfig {

    public JdaEmbed ticket = new JdaEmbed(
            0,
            0,
            0,
            "Create Ticket",
            "",
            "**Select the ticket type that you want to create**",
            Arrays.asList(new JdaField("Warnings!",
                    "We do not solve any problems related to any lost or stolen items!\n" +
                            "All the complains are made on https://original.gg/complain!",
                    true)),
            new ArrayList<>()
    );

    public JdaEmbed unbanTicket = new JdaEmbed(
            0,
            0,
            0,
            "Create Unban Ticket",
            "",
            "",
            Arrays.asList(new JdaField("Warnings!",
                    "If you have been banned for advertising you will not be able to be unbanned!\n" +
                            "If you have been banned as a result of your account being compromised you will need to provide proof that you have secured your account",
                    true)),
            new ArrayList<>()
    );


    public JdaEmbed ticketGreeting = new JdaEmbed(
            0,
            255,
            0,
            "%name%",
            "%avatar%",
            "A staff member will answer your ticket as soon as possible.\n" +
                    "Please state your problem bellow!\n" +
                    "If you need the help of a manager please press the button bellow!",
            new ArrayList<>(),
            Arrays.asList(new Button(JDAButtonType.DANGER, "manager", "\uD83C\uDF93 Manager"),
                    new Button(JDAButtonType.PRIMARY, "close-ticket", "\uD83D\uDD12 Close ticket"))
    );

    public JdaEmbed unbanTicketGreeting = new JdaEmbed(
            0,
            255,
            0,
            "%name%",
            "%avatar%",
            "Please state why you thing you need to be unbanned.\n" +
                    "The staff member that have banned you have been notified.",
            new ArrayList<>(),
            Arrays.asList(
                    new Button(JDAButtonType.PRIMARY, "close-ticket", "\uD83D\uDD12 Close ticket"),
                    new Button(JDAButtonType.PRIMARY, "unban", "\uD83D\uDD13 Unban")
            )
    );

    public JdaEmbed unbanDetails = new JdaEmbed(
            255,
            0,
            0,
            "%name%",
            "",
            "Username: %name%\n" +
                    "ID: %id%\n" +
                    "Banned By: %banned_by_name%\n" +
                    "Banned By ID: %banned_by_id%\n" +
                    "Reason: %reason%",
            new ArrayList<>(),
            new ArrayList<>()
    );

    public JdaEmbed closingTicket = JdaEmbed.red(
            "Ticket",
            "This ticket will be closed in 5 seconds"
    );

    public JdaEmbed invalidID = JdaEmbed.red(
            "Ban",
            "This is not a valid discord ID"
    );

    public JdaEmbed alreadyBanned = JdaEmbed.red(
            "Ban",
            "User is already banned"
    );

    public JdaEmbed invalidUser = JdaEmbed.red(
            "Users",
            "This is not a valid user"
    );

    public JdaEmbed invalidBannedRole = JdaEmbed.red(
            "Roles",
            "The banned role is invalid please check it"
    );

    public JdaEmbed userBanned = JdaEmbed.green(
            "Roles",
            "The user %name% has been banned"
    );

    public JdaEmbed helpEmbed = JdaEmbed.black(
            "Help",
            "+**General**\n" +
                    "+help\n" +
                    "\n**Moderation**\n" +
                    "+ban [user_id] [reason]\n" +
                    "+unban [user_id]\n" +
                    "+checkBan [user_id]\n" +
                    "+banDetails [id]\n" +
                    "+history [user_id]\n" +
                    "\n**Tickets**\n" +
                    "+tickets [user_id]\n" +
                    "+transcript [id]\n" +
                    "+add [user_id]\n" +
                    "+close\n" +
                    "\n**Management**\n" +
                    "+setup\n" +
                    "\n" +
                    "[] - Mandatory arguments\n"
                    + "<> - Optional / Contextual arguments"
    );

    public JdaEmbed setupFinished = JdaEmbed.green(
            "Setup",
            "Setup finished"
    );

    public JdaEmbed notBanned = JdaEmbed.red(
            "Ban",
            "User is not banned"
    );

    public JdaEmbed unBanned = JdaEmbed.green(
            "UnBan",
            "User %name% is now unbanned. %roles_1%/%roles_2% roles have been restored"
    );


    public JdaEmbed notTicket = JdaEmbed.red(
            "Ticket",
            "This chanel is not a valid ticket channel"
    );

    public JdaEmbed alreadyPingedManager = JdaEmbed.red(
            "Ticket",
            "You have already pinged the manager on this ticket. Please be patient we will answer your problem as soon as possible"
    );

    public JdaEmbed error = JdaEmbed.red(
            "Error",
            "An error occurred while performing this action. Please contact the author in regards to this."
    );

    public JdaEmbed addedToTicket = JdaEmbed.green(
            "Tickets",
            "Added %name% to this ticket."
    );

    public JdaEmbed cannotBan = JdaEmbed.red(
            "Ban",
            "I can not ban this use because he has a higher rank then me."
    );

    public JdaEmbed cannotUnban = JdaEmbed.red(
            "Ban",
            "I can not unban this use because he has a higher rank then me."
    );

    public JdaEmbed invalidTicketID = JdaEmbed.red(
            "Ticket",
            "This is not a valid ticket id."
    );

    public JdaEmbed invalidBanID = JdaEmbed.red(
            "Bans",
            "This is not a valid ban id."
    );

    public JdaEmbed closedTicket = JdaEmbed.green(
            "Ticket",
            "Your ticket has been closed. You can access your transcript by using the command `+transcript %id%`."
    );

    public JdaEmbed notAllowedToAccessTranscript = JdaEmbed.red(
            "Transcript",
            "You are not allowed to access this transcript."
    );

    public JdaEmbed transcript = JdaEmbed.black(
            "Transcript",
            "You can access the transcript with id %id% at %url%."
    );

    public JdaEmbed tickets = JdaEmbed.green(
            "%name%'s Tickets (last 10)",
            "You can use +transcript [id] to see the transcript on one specific ticket\n"
    );

    public String ticketsEntry = "[%id%] Ticket %type% on date %date%";

    public JdaEmbed bans = JdaEmbed.green(
            "%name%'s Bans (last 10)",
            "You can use +banDetails [id] to see more about one specific ban\n"
    );

    public String bansEntry = "[%id%] Ban on date %date% for `%reason%`";


}
