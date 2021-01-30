package kitchenpos;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@ActiveProfiles("test")
public class DatabaseCleanup {

    private final List<String> tableNames = Arrays.asList("MENU", "MENU_GROUP", "MENU_PRODUCT", "ORDERS", "ORDER_LINE_ITEM", "ORDER_TABLE", "PRODUCT", "TABLE_GROUP");
    private final JdbcTemplate jdbcTemplate;

    public DatabaseCleanup(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
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
