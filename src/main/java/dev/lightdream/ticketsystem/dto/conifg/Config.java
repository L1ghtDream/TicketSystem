package dev.lightdream.ticketsystem.dto.conifg;

import dev.lightdream.jdaextension.dto.JDAEmbed;
import dev.lightdream.jdaextension.dto.JDAField;
import dev.lightdream.ticketsystem.dto.TicketGroup;
import dev.lightdream.ticketsystem.dto.TicketType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class Config {

    public boolean debug = false;
    public List<TicketGroup> ticketGroups = Arrays.asList(
            // General tickets
            new TicketGroup(
                    0L,
                    Arrays.asList(
                            new TicketType("\uD83D\uDCDC General", "general", 0L, "general",new ArrayList<>()),
                            new TicketType("\uD83C\uDFC6 Tops", "tops", 0L,"general", new ArrayList<>()),
                            new TicketType("\uD83D\uDCB0 Donations", "donations", 0L, "general",new ArrayList<>()),
                            new TicketType("\uD83D\uDD12 Lost Passwords", "lost-passwords", 0L, "general",new ArrayList<>()),
                            new TicketType(":CFbug: Report a Bug", "bug", 0L, "dialogue",new ArrayList<>(), Arrays.asList(
                                    "Please give a brief description of the bug.",
                                    "Where does it happen?",
                                    "What is the expected behaviour?",
                                    "What is the actual behaviour?",
                                    "What are the steps to reproduce the bug? (video/screenshot)"
                            )),
                            new TicketType("❗ File a Complain", "complain", 0L, "dialogue",new ArrayList<>(), Arrays.asList(
                                    "Please give a brief description of the complaint.",
                                    "Who you are complaining against?",
                                    "What is the rank of the person who you are complaining against?",
                                    "Any proof to support the complaint? (video/screenshot)"
                            )),
                            new TicketType("\uD83D\uDC6E\u200D♂️ Apply", "apply", 0L, "dialogue",new ArrayList<>(), Arrays.asList(
"options~~~option~~~discord-staff,tiktoker,youtuber,developer|||What is the position you are applying for? (open positions are: discord-staff, tiktoker, youtuber, developer)",
"How old are you?",

"option~~~discord-staff|||Please give a brief description of the reasons you should be accepted.",

"option~~~youtuber|||How many subscribers do you have? (at minimum x)",
"option~~~youtuber|||How many videos you have on our network? (at minimum x)",
"option~~~youtuber|||How many views have you tantalized on our network? (at minimum x)",
"option~~~youtuber|||Why are you interested in recording videos specifically on our server?",
"option~~~youtuber|||What's your average engagement rate for a video? (views, likes)",
"option~~~youtuber|||Do you seek monetary compensation for your videos?",
"option~~~youtuber|||What type of videos do you normally record?",
"option~~~youtuber|||What do you hope to achieve as a youtuber on our server?",
"option~~~youtuber|||Link your channel.",

"option~~~tiktoker|||How many followers do you have? (at minimum x)",
"option~~~tiktoker|||How many videos you have on our network? (at minimum x)",
"option~~~tiktoker|||How many views have you tantalized on our network? (at minimum x)",
"option~~~tiktoker|||Why are you interested in recording videos specifically on our server?",
"option~~~tiktoker|||What's your average engagement rate for a video? (views, likes)",
"option~~~tiktoker|||Do you seek monetary compensation for your videos?",
"option~~~tiktoker|||What type of videos do you normally record?",
"option~~~tiktoker|||What do you hope to achieve as a youtuber on our server?",
"option~~~tiktoker|||Link your channel.",

"option~~~developer|||What is your experience? (mainly in Java and frameworks that have to do with minecraft)",
"option~~~developer|||Link to your portfolio."
                            ))
                    ),
                    JDAEmbed.black(
                            "Create Ticket",
                            Arrays.asList(new JDAField("Warnings!",
                                    "We do not solve any problems related to any lost or stolen items!\n" +
                                            "You can only apply for discord and miscellaneous positions on discord.\n" +
                                            "If you want to apply for minecraft staff you can do so at https://original.gg/apply",
                                    true))
                    )
            ),

            // Unban tickets
            new TicketGroup(
                    0L,
                    Arrays.asList(
                            new TicketType("\uD83D\uDD13 Unban Request", "unban-request", 0L,"unban", new ArrayList<>())
                    ),
                    JDAEmbed.black(
                            "Create Unban Ticket",
                            Arrays.asList(new JDAField("Warnings!",
                                    "If you have been banned for advertising you will not be able to be unbanned!\n" +
                                            "If you have been banned as a result of your account being compromised you will need to provide proof that you have secured your account",
                                    true))
                    )
            )
    );
    public Long bannedRank = 0L;
    public Long managerPingRank = 0L;
    public String dateFormat = "HH:mm dd MMM yyyy";

    @Nullable
    public TicketType getTicketTypeByID(String id) {
        for (TicketGroup group : ticketGroups) {
            for (TicketType type : group.ticketTypes) {
                if (type.id.equals(id)) {
                    return type;
                }
            }
        }
        return null;
    }

    @Nullable
    public TicketType getTicketTypeByCategoryID(Long categoryID) {
        for (TicketGroup group : ticketGroups) {
            for (TicketType type : group.ticketTypes) {
                if (type.categoryID.equals(categoryID)) {
                    return type;
                }
            }
        }
        return null;
    }

}
