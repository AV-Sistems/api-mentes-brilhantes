package br.com.avsistems.config;

import br.com.avsistems.entity.UserEntity;
import br.com.avsistems.repository.UserRepository;
import br.com.avsistems.type.UserType;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

@ApplicationScoped
public class DefaultAdminInitializer {

    private static final Logger LOG = Logger.getLogger(DefaultAdminInitializer.class);
    private static final String DEFAULT_ADMIN_NAME = "Antonio";
    private static final String DEFAULT_ADMIN_EMAIL = "antoniosvj@avsistems.com.br";
    private static final String DEFAULT_ADMIN_PASSWORD = "Antlor.1709";

    @Inject
    UserRepository userRepository;

    @Inject
    EntityManager entityManager;

    void onStart(@Observes StartupEvent event) {
        ensureDefaultAdminExists();
    }

    @Transactional
    void ensureDefaultAdminExists() {
        Number existingUsers = (Number) entityManager
                .createNativeQuery("select count(*) from users where lower(email) = lower(?1)")
                .setParameter(1, DEFAULT_ADMIN_EMAIL)
                .getSingleResult();

        if (existingUsers != null && existingUsers.longValue() > 0) {
            LOG.infof("Usuário administrador padrão já existe: %s", DEFAULT_ADMIN_EMAIL);
            return;
        }

        UserEntity admin = new UserEntity();
        admin.name = DEFAULT_ADMIN_NAME;
        admin.email = DEFAULT_ADMIN_EMAIL;
        admin.password = BcryptUtil.bcryptHash(DEFAULT_ADMIN_PASSWORD);
        admin.userType = UserType.ADMIN;
        admin.active = true;
        admin.totalPoints = 0;
        admin.redeemablePoints = 0;

        userRepository.persist(admin);
        LOG.infof("Usuário administrador padrão criado com sucesso: %s", DEFAULT_ADMIN_EMAIL);
    }
}

