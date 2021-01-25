package kitchenpos.utils;

import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;

@Service
@ActiveProfiles("test")
public class DatabaseCleanup {

    private final Flyway flyway;

    public DatabaseCleanup(Flyway flyway) {
        this.flyway = flyway;
    }

    public void execute() {
        flyway.clean();
        flyway.migrate();
    }
}
