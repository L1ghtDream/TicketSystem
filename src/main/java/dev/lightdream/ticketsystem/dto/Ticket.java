package dev.lightdream.ticketsystem.dto;

import dev.lightdream.databasemanager.DatabaseMain;
import dev.lightdream.databasemanager.dto.DatabaseEntry;

public class Ticket extends DatabaseEntry {

    public String type;
    public Long id;

    public Ticket(DatabaseMain main) {
        super(main);
    }


}
