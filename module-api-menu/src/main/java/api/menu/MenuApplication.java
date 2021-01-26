package api.menu;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@EnableJpaRepositories(basePackages = {"domain.menu"})
@EntityScan(basePackages = {"domain.menu"})
@EnableJpaAuditing
@SpringBootApplication
public class MenuApplication implements CommandLineRunner {

	@Autowired
	private DataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(MenuApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Flyway.configure().baselineOnMigrate(true).dataSource(dataSource).load().migrate();
	}
}
