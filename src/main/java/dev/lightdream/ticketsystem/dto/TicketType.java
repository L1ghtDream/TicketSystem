package dev.lightdream.ticketsystem.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class TicketType {

    public String name;
    public String id;
    public Long categoryID;
    public List<Long> associatedRanks;

}
