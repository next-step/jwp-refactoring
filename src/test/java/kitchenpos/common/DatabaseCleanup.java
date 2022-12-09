package kitchenpos.common;

import org.flywaydb.core.internal.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;

@Service
@ActiveProfiles("test")
public class DatabaseCleanup {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseCleanup.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final List<Pair<String, String>> tableNameWithAutoIncrementColumns;

    public DatabaseCleanup(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        tableNameWithAutoIncrementColumns = Arrays.asList(Pair.of("orders", "id"),
                                                          Pair.of("order_line_item", "seq"),
                                                          Pair.of("menu", "id"),
                                                          Pair.of("menu_group", "id"),
                                                          Pair.of("menu_product", "seq"),
                                                          Pair.of("order_table", "id"),
                                                          Pair.of("table_group", "id"),
                                                          Pair.of("product", "id"));
    }

    @Transactional
    public void execute() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE", PreparedStatement::executeUpdate);

        for (Pair<String, String> tableNameWithAutoIncrementColumn : tableNameWithAutoIncrementColumns) {
            executeTruncateTable(tableNameWithAutoIncrementColumn);
            executeResetAutoIncrement(tableNameWithAutoIncrementColumn);
        }

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE", PreparedStatement::executeUpdate);
    }

    private void executeTruncateTable(Pair<String, String> tableNameWithAutoIncrementColumn) {
        String tableName = tableNameWithAutoIncrementColumn.getLeft();
        jdbcTemplate.execute("TRUNCATE TABLE " + tableName, PreparedStatement::executeUpdate);
    }

    private void executeResetAutoIncrement(Pair<String, String> tableNameWithAutoIncrementColumn) {
        String tableName = tableNameWithAutoIncrementColumn.getLeft();
        String column = tableNameWithAutoIncrementColumn.getRight();
        jdbcTemplate.execute("ALTER TABLE " + tableName + " ALTER COLUMN " + column + " RESTART WITH 1",
                             PreparedStatement::executeUpdate);
    }
}
