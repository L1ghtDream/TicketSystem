package dev.lightdream.ticketsystem;

import net.dv8tion.jda.api.JDA;

public interface ISecurity {

    default void sayHello() {
        System.out.println("Hello from the default security implementation!");
    }

    default String getConfigPath() {
        return "/config/";
    }

    default JDA getBot() {
        return null;
    }

}
