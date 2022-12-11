package kitchenpos;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class BaseAcceptanceTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private List<TableInformation> tableInformations = Arrays.asList(new TableInformation("menu_group", "id"),
            new TableInformation("menu", "id"),
            new TableInformation("menu_product", "seq"),
            new TableInformation("product", "id"),
            new TableInformation("order_line_item", "seq"),
            new TableInformation("orders", "id"),
            new TableInformation("order_table", "id"),
            new TableInformation("table_group", "id"));

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        tableInformations.forEach(info -> {
            jdbcTemplate.execute("TRUNCATE TABLE " + info.getName());
            jdbcTemplate.execute("ALTER TABLE " + info.getName() + " ALTER COLUMN " + info.getId() + " RESTART WITH 1");
        });
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    private class TableInformation {
        private String name;
        private String id;

        public TableInformation(String name, String id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }
    }
}
