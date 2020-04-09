package ca.mcgill.cooperator.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestFlywayConfig {
    @Bean
    public FlywayMigrationStrategy cleanMigrateStrategy() {
        FlywayMigrationStrategy strategy =
                new FlywayMigrationStrategy() {
                    @Override
                    public void migrate(Flyway flyway) {
                        flyway.clean();
                        flyway.migrate();
                    }
                };

        return strategy;
    }
}
