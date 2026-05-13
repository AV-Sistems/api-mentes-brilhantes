package br.com.avsistems.config;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

import java.util.TimeZone;

@ApplicationScoped
public class TimeZoneInitializer {

    void onStart(@Observes StartupEvent event) {
        TimeZone.setDefault(TimeZone.getTimeZone(AppTime.ZONE_ID));
        System.setProperty("user.timezone", AppTime.ZONE_ID.getId());
    }
}
