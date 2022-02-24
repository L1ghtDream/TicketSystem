package dev.lightdream.ticketsystem.dto;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class Config {

    public boolean debug = true;
    public Long ticketsChanel = 946527951233622077L;
    public List<TicketType> ticketTypes = Arrays.asList(
            new TicketType("\uD83D\uDCDC General", "general", 946528774084755477L, Arrays.asList(
                    946529041505193984L
            )),
            new TicketType("\uD83C\uDFC6 Topuri", "tops", 946540672813518868L, Arrays.asList(
                    946529041505193984L
            )),
            new TicketType("\uD83D\uDCB0 Donatii", "donations", 946540700793700422L, Arrays.asList(
                    946529041505193984L
            )),
            new TicketType("\uD83D\uDD12 Parole Pierdute", "lost-passwords", 946540713326297088L, Arrays.asList(
                    946529041505193984L
            ))
    );

}
