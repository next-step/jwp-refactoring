package kitchenpos;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Service
@ActiveProfiles("test")
public class DatabaseCleanup {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final List<String> tableNames;

    public DatabaseCleanup(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        tableNames = Arrays.asList("orders",
                "order_line_item",
                "menu",
                "menu_group",
                "menu_product",
                "order_table",
                "table_group",
                "product");
    }

    @Transactional
    public void execute() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE", PreparedStatement::executeUpdate);

        for (String tableName : tableNames) {
            String keyColumn = getKeyColumn(tableName);
            jdbcTemplate.execute("TRUNCATE TABLE " + tableName, PreparedStatement::executeUpdate);
            jdbcTemplate.execute("ALTER TABLE " + tableName + " ALTER COLUMN " + keyColumn + " RESTART WITH 1", PreparedStatement::executeUpdate);
        }

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE", PreparedStatement::executeUpdate);
    }

    private String getKeyColumn(String tableName) {
        if (tableName.equals("order_line_item") || tableName.equals("menu_product")) {
            return "seq";
        }
        return "id";
    }
}
