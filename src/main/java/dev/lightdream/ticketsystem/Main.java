package dev.lightdream.ticketsystem;

import dev.lightdream.databasemanager.DatabaseMain;
import dev.lightdream.databasemanager.database.IDatabaseManager;
import dev.lightdream.databasemanager.dto.DriverConfig;
import dev.lightdream.databasemanager.dto.SQLConfig;
import dev.lightdream.filemanager.FileManager;
import dev.lightdream.filemanager.FileManagerMain;
import dev.lightdream.jdaextension.JDAExtensionMain;
import dev.lightdream.jdaextension.commands.commands.HelpCommand;
import dev.lightdream.jdaextension.commands.commands.StatsCommand;
import dev.lightdream.jdaextension.dto.JdaEmbed;
import dev.lightdream.jdaextension.managers.DiscordCommandManager;
import dev.lightdream.logger.Debugger;
import dev.lightdream.logger.LoggableMain;
import dev.lightdream.logger.Logger;
import dev.lightdream.ticketsystem.commands.*;
import dev.lightdream.ticketsystem.dto.Config;
import dev.lightdream.ticketsystem.dto.JDAConfig;
import dev.lightdream.ticketsystem.dto.Lang;
import dev.lightdream.ticketsystem.manager.DatabaseManager;
import dev.lightdream.ticketsystem.manager.DiscordEventManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.Arrays;

public class Main implements DatabaseMain, LoggableMain, FileManagerMain, JDAExtensionMain {

    public static Main instance;

    public Config config;
    public SQLConfig sqlConfig;
    public DriverConfig driverConfig;
    public JDAConfig jdaConfig;
    public Lang lang;

    public FileManager fileManager;
    public DatabaseManager databaseManager;
    public DiscordCommandManager discordCommandManager;
    public DiscordEventManager discordEventManager;

    public JDA bot;

    public void onEnable() {
        Debugger.init(this);
        Logger.init(this);

        instance = this;

        fileManager = new FileManager(this, FileManager.PersistType.YAML);
        loadConfigs();

        databaseManager = new DatabaseManager(this);

        try {
            bot = JDABuilder.createDefault(jdaConfig.token).build();
        } catch (LoginException e) {
            Logger.error("The bot token seems to be missing or incorrect, please check if it!");
            return;
        }

        discordCommandManager = new DiscordCommandManager(this, Arrays.asList(
                new HelpCommand(this),
                new StatsCommand(this),
                new CloseCommand(this),
                new BanCommand(this),
                new UnbanCommand(this),
                new AddCommand(this),
                new SetupCommand(this)
        ));
        discordEventManager = new DiscordEventManager(this);

        Logger.good("Ticket System Bot (by https://github.com/L1ghtDream) has started!");
    }

    public void loadConfigs() {
        config = fileManager.load(Config.class);
        sqlConfig = fileManager.load(SQLConfig.class);
        driverConfig = fileManager.load(DriverConfig.class);
        jdaConfig = fileManager.load(JDAConfig.class);
        lang = fileManager.load(Lang.class);
    }

    @Override
    public File getDataFolder() {
        return new File(System.getProperty("user.dir") + "/config/");
    }

    @Override
    public SQLConfig getSqlConfig() {
        return sqlConfig;
    }

    @Override
    public DriverConfig getDriverConfig() {
        return driverConfig;
    }

    @Override
    public IDatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public boolean debug() {
        return config.debug;
    }

    @Override
    public void log(String s) {
        System.out.println(s);
    }

    @Override
    public JDA getBot() {
        return bot;
    }

    @Override
    public JDAConfig getJDAConfig() {
        return jdaConfig;
    }

    @Override
    public DiscordCommandManager getDiscordCommandManager() {
        return discordCommandManager;
    }

    @Override
    public String getPrefix() {
        return config.prefix;
    }

    @Override
    public JdaEmbed getHelpEmbed() {
        return jdaConfig.helpEmbed;
    }
}
