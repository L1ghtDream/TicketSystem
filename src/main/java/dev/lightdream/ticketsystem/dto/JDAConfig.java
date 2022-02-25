package dev.lightdream.ticketsystem.dto;

import dev.lightdream.jdaextension.dto.Button;
import dev.lightdream.jdaextension.dto.JdaEmbed;
import dev.lightdream.jdaextension.dto.JdaField;
import dev.lightdream.jdaextension.enums.JDAButtonType;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class JDAConfig extends dev.lightdream.jdaextension.dto.JDAConfig {

    public JdaEmbed ticket = new JdaEmbed(0,
            0,
            0,
            "Create Ticket",
            "",
            "**Selete the ticket type that you want to create**",
            Arrays.asList(new JdaField("Warnings!",
                    "We do not solve any problems related to any lost or stolen items!\nAll the complains are made on https://original.gg/complain!",
                    true)),
            new ArrayList<>());


    public JdaEmbed ticketGreeting = new JdaEmbed(0,
            255,
            0,
            "%name%",
            "%avatar%",
            "A staff member will answer your ticket as soon as possible.\nPlease state your problem bellow!\nIf you need the help of a manager please press the button bellow!",
            new ArrayList<>(),
            Arrays.asList(new Button(JDAButtonType.DANGER, "manager", "\uD83C\uDF93 Manager"),
                    new Button(JDAButtonType.PRIMARY, "close-ticket", "\uD83D\uDD12 Inchide ticket-ul")));

    public JdaEmbed banTicketGreeting = new JdaEmbed(0,
            255,
            0,
            "%name%",
            "%avatar%",
            "Please state why you thing you need to be unbanned.\nThe staff member that have banned you have been notified.",
            new ArrayList<>(),
            Arrays.asList(new Button(JDAButtonType.DANGER, "manager", "\uD83C\uDF93 Manager"),
                    new Button(JDAButtonType.PRIMARY, "close-ticket", "\uD83D\uDD12 Inchide ticket-ul")));

    public JdaEmbed banDetails = new JdaEmbed(255,
            0,
            0,
            "%name%",
            "%avatar%",
            "Username: %name%\nID: %id%\nBanned By: %banned_by_name%\nBanned By ID: %banned_by_id%\nReason: %reason%",
            new ArrayList<>(),
            new ArrayList<>());

    public JdaEmbed closingTicket = new JdaEmbed(255,
            0,
            0,
            "Ticket",
            "",
            "This ticket will be closed in 5 seconds",
            new ArrayList<>(),
            new ArrayList<>());


}
