package dev.lightdream.ticketsystem.dto;

import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.databasemanager.dto.DatabaseEntry;
import dev.lightdream.ticketsystem.Main;

@DatabaseTable(table = "tickets")
public class Ticket extends DatabaseEntry {

    @DatabaseField(columnName = "type")
    public String type;
    @DatabaseField(columnName = "channel_id")
    public Long channelID;
    @DatabaseField(columnName = "creator_id")
    public Long creatorID;
    @DatabaseField(columnName = "pinged_manager")
    public boolean pingedManager;

    public Ticket(String type, Long channelID, Long creatorID) {
        super(Main.instance);
        this.type = type;
        this.channelID = channelID;
        this.pingedManager = false;
        this.creatorID = creatorID;
    }

    @SuppressWarnings("unused")
    public Ticket() {
        super(Main.instance);
    }


}
