package dev.lightdream.ticketsystem.dto;

import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.databasemanager.dto.DatabaseEntry;
import dev.lightdream.ticketsystem.Main;
import lombok.SneakyThrows;
import me.kaimu.hastebin.Hastebin;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;

import java.util.ArrayList;
import java.util.List;

@DatabaseTable(table = "transcripts")
public class Transcript extends DatabaseEntry {

    @DatabaseField(columnName = "ticket_id")
    public int ticketID;
    @DatabaseField(columnName = "messages")
    public List<String> messages;


    public Transcript(int ticketID) {
        super(Main.instance);
        this.ticketID = ticketID;
        this.messages = new ArrayList<>();
    }

    @SuppressWarnings("unused")
    public Transcript() {
        super(Main.instance);
    }

    public void record(User user, String record) {
        messages.add(
                user.getName() + " (" + user.getId() + "): " + record
        );
        save();
    }

    @SneakyThrows
    public void send(User user) {
        Hastebin hastebin = new Hastebin();

        StringBuilder text = new StringBuilder();
        messages.forEach(message -> {
            text.append(message);
            text.append("\n");
        });

        String url = hastebin.post(text.toString(), false);

        user.openPrivateChannel()
                .queue(channel -> channel.sendMessageEmbeds(Main.instance.jdaConfig.transcript
                                .parse("id", String.valueOf(ticketID))
                                .parse("url", url)
                                .build().build())
                        .queue(null,
                                new ErrorHandler().handle(ErrorResponse.CANNOT_SEND_TO_USER,
                                        e -> {
                                            //empty
                                        })
                        )
                );
    }
}
