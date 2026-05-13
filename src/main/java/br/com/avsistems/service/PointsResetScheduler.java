package br.com.avsistems.service;

import br.com.avsistems.repository.UserRepository;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

@ApplicationScoped
public class PointsResetScheduler {

    private static final Logger LOG = Logger.getLogger(PointsResetScheduler.class);

    @Inject
    UserRepository userRepository;

    // Executa todo ano, em 01/01 as 01:00 no horario de Sao Paulo.
    @Transactional
    @Scheduled(cron = "0 0 1 1 1 ?", identity = "yearly-points-reset", timeZone = "America/Sao_Paulo")
    void resetYearlyAndRedeemablePoints() {
        long updated = userRepository.resetAllPoints();
        LOG.infof("Reset anual de pontos executado. Usuarios afetados: %d", updated);
    }
}
