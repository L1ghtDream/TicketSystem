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

    /**
     * @param bannedBy The user who banned the user
     * @param reason   reason
     * @param duration in ms, 0 for permanent
     */
    default void banUser(Long user, Long bannedBy, String reason, Long duration) {

    }

    default void unbanUser(Long user) {

    }

}
