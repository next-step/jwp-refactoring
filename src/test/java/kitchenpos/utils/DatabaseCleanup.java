package kitchenpos.utils;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@ActiveProfiles("test")
public class DatabaseCleanup {
    private final JdbcTemplate jdbcTemplate;
    // 테스트를 위한 하드코딩으로 이해 부탁드립니다.
    private final List<String> tableNames = Arrays.asList("menu", "menu_group", "orders", "order_table", "product", "table_group", "order_line_item", "menu_product");

    public DatabaseCleanup(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void execute() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");

        for (String tableName : tableNames) {
            jdbcTemplate.execute("TRUNCATE TABLE " + tableName + " RESTART IDENTITY;");
        }
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }
}
