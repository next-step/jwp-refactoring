package kitchenpos;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;

@Service
@ActiveProfiles("test")
public class DatabaseCleanup {
    private final String[] tableNames = {"menu", "menu_group", "menu_product", "orders", "order_line_item", "order_table", "product", "table_group"};

    private final JdbcTemplate jdbcTemplate;

    public DatabaseCleanup(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void execute() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        for (String tableName : tableNames) {
            jdbcTemplate.execute("TRUNCATE TABLE " + tableName);
            try {
                jdbcTemplate.execute("ALTER TABLE " + tableName + " ALTER COLUMN ID RESTART WITH 1");
            } catch ( Exception e ) {
                jdbcTemplate.execute("ALTER TABLE " + tableName + " ALTER COLUMN SEQ RESTART WITH 1");
            }
        }
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }
}
