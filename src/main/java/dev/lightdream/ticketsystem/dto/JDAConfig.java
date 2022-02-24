package dev.lightdream.ticketsystem.dto;

import dev.lightdream.jdaextension.dto.JdaEmbed;
import dev.lightdream.jdaextension.dto.JdaField;
import jdk.nashorn.internal.scripts.JD;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class JDAConfig extends dev.lightdream.jdaextension.dto.JDAConfig {

    public JdaEmbed ticket  = new JdaEmbed(
            0,
            0,
            0,
            "Create Ticket",
            "",
            "**Salut! Selecteaza tipul de ticket de care ai nevoie!**",
            Arrays.asList(
                    new JdaField("Atentie!", "Nu raspundem de itemele pierdute!\n" +
                            "Reclamatiile se fac pe panel!", true)
            ),
            new ArrayList<>()
    );

}
