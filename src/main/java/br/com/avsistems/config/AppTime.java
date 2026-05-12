package br.com.avsistems.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public final class AppTime {

    public static final ZoneId ZONE_ID = ZoneId.of("America/Sao_Paulo");

    private AppTime() {
    }

    public static LocalDateTime now() {
        return LocalDateTime.now(ZONE_ID);
    }

    public static LocalDate today() {
        return LocalDate.now(ZONE_ID);
    }
}
