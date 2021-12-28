package kitchenpos.utils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Service
@ActiveProfiles("test")
public class DatabaseCleanup {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public DatabaseCleanup(final DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Transactional
    public void execute() {
        final List<String> tableNames = extractTableNames();
        execute("SET REFERENTIAL_INTEGRITY FALSE");

        for (final String tableName : tableNames) {
            execute("TRUNCATE TABLE " + tableName);
        }

        execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    private List<String> extractTableNames() {
        return jdbcTemplate.query("SHOW TABLES", (resultSet, rowNumber) -> resultSet.getString(1)).stream()
            .filter(it -> !it.equals("flyway_schema_history"))
            .collect(Collectors.toList());
    }

    private void execute(final String query) {
        jdbcTemplate.update(query, Collections.emptyMap());
    }
}
