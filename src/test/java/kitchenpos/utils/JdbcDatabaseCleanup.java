package kitchenpos.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@ActiveProfiles("test")
public class JdbcDatabaseCleanup implements DatabaseCleanup {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private List<JdbcTable> tables;

    @Override
    public void afterPropertiesSet() {
        tables = Arrays.asList(
                new JdbcTable("product", "id"),
                new JdbcTable("order_table", "id"),
                new JdbcTable("orders", "id"),
                new JdbcTable("menu_group", "id"),
                new JdbcTable("menu", "id"),
                new JdbcTable("order_line_item", "seq"),
                new JdbcTable("menu_product", "seq"),
                new JdbcTable("table_group", "id")
        );
    }

    @Transactional
    public void execute() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");

        for (JdbcTable table : tables) {
            jdbcTemplate.execute("TRUNCATE TABLE " + table.name);
            jdbcTemplate.execute("ALTER TABLE " + table.name + " ALTER COLUMN " + table.id + " RESTART WITH 1");
        }

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    private static class JdbcTable {
        private final String name;
        private final String id;

        public JdbcTable(String name, String id) {
            this.name = name;
            this.id = id;
        }
    }
}
