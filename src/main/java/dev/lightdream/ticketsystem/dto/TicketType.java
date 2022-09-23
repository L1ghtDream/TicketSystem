package dev.lightdream.ticketsystem.dto;

import java.util.ArrayList;
import java.util.List;

public class TicketType {

    public String name;
    public String id;
    public Long categoryID;
    public List<Long> associatedRanks;
    /**
     * Registered handlers: general, unban, dialogue
     */
    public String handler = "general";
    /**
     * If the handler is set as dialogue than this will need to be provided as well
     */
    public List<String> questions;

    public TicketType() {
    }

    public TicketType(String name, String id, Long categoryID, String handler, List<Long> associatedRanks, List<String> questions) {
        this.name = name;
        this.id = id;
        this.categoryID = categoryID;
        this.associatedRanks = associatedRanks;
        this.questions = questions;
        this.handler = handler;
    }

    public TicketType(String name, String id, Long categoryID, String handler, List<Long> associatedRanks) {
        this(name, id, categoryID, handler, associatedRanks, new ArrayList<>());
    }
}
