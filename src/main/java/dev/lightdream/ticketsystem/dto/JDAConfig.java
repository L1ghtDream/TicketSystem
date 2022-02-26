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
            "**Selete the ticket type that you want to create**",
            Arrays.asList(new JdaField("Warnings!",
                    "We do not solve any problems related to any lost or stolen items!\n" +
                            "All the complains are made on https://original.gg/complain!",
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
                    new Button(JDAButtonType.PRIMARY, "close-ticket", "\uD83D\uDD12 Inchide ticket-ul"))
    );

    public JdaEmbed banTicketGreeting = new JdaEmbed(
            0,
            255,
            0,
            "%name%",
            "%avatar%",
            "Please state why you thing you need to be unbanned.\n" +
                    "The staff member that have banned you have been notified.",
            new ArrayList<>(),
            Arrays.asList(new Button(JDAButtonType.DANGER, "manager", "\uD83C\uDF93 Manager"),
                    new Button(JDAButtonType.PRIMARY, "close-ticket", "\uD83D\uDD12 Inchide ticket-ul"))
    );

    public JdaEmbed banDetails = new JdaEmbed(
            255,
            0,
            0,
            "%name%",
            "%avatar%",
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
            "+help\n" +
                    "+link [username]\n" +
                    "+unlink <username>\n" +
                    "+changePassword <username> [newPassword] - In DMs\n" +
                    "+accounts <discordID>\n" +
                    "+stats\n" +
                    "+unregister <discordID>\n" +
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
            "User %name% is not unbanned. %roles_1%/%roles_2% roles have been restored"
    );


}
