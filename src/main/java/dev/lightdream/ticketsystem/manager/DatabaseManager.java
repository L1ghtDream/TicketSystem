package dev.lightdream.ticketsystem.manager;

import dev.lightdream.databasemanager.DatabaseMain;
import dev.lightdream.databasemanager.OrderBy;
import dev.lightdream.databasemanager.database.ProgrammaticHikariDatabaseManager;
import dev.lightdream.databasemanager.dto.QueryConstrains;
import dev.lightdream.ticketsystem.dto.BanRecord;
import org.jetbrains.annotations.Nullable;

public class DatabaseManager extends ProgrammaticHikariDatabaseManager {
    public DatabaseManager(DatabaseMain main) {
        super(main);
    }

    @Override
    public void setup() {
        setup(BanRecord.class);
    }

    public @Nullable BanRecord getBan(Long id) {
        return get(BanRecord.class).query(new QueryConstrains().and(new QueryConstrains().equals("user_id", id),
                        new QueryConstrains().equals("active", true)))
                .order(OrderBy.DESCENDENT("timestamp"))
                .limit(1)
                .query()
                .stream()
                .findAny()
                .orElse(null);
    }

}
