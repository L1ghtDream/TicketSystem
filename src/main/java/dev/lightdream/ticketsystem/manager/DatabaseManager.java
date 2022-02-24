package dev.lightdream.ticketsystem.manager;

import dev.lightdream.databasemanager.DatabaseMain;
import dev.lightdream.databasemanager.database.ProgrammaticHikariDatabaseManager;

public class DatabaseManager extends ProgrammaticHikariDatabaseManager {
    public DatabaseManager(DatabaseMain main) {
        super(main);
    }

    @Override
    public void setup() {

    }
}
