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
                            new TicketType("\uD83D\uDCDC General", "general", 0L, new ArrayList<>()),
                            new TicketType("\uD83C\uDFC6 Tops", "tops", 0L, new ArrayList<>()),
                            new TicketType("\uD83D\uDCB0 Donations", "donations", 0L, new ArrayList<>()),
                            new TicketType("\uD83D\uDD12 Lost Passwords", "lost-passwords", 0L, new ArrayList<>()),
                            new TicketType(":CFbug: Report a Bug", "bug", 0L, new ArrayList<>()),
                            new TicketType("❗ File a Complain", "complain", 0L, new ArrayList<>()),
                            new TicketType("\uD83D\uDC6E\u200D♂️ Apply", "apply", 0L, new ArrayList<>())
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
                            new TicketType("\uD83D\uDD13 Unban Request", "unban-request", 0L, new ArrayList<>())
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
