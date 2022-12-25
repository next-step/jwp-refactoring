package kitchenpos.utils;


import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Service
@ActiveProfiles("test")
public class DatabaseCleanup {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final List<String> tableNames;

    public DatabaseCleanup(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        tableNames = getTableNames(jdbcTemplate);
    }

    @Transactional
    public void execute() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");

        try {
            for (String tableName : tableNames) {
                jdbcTemplate.execute("TRUNCATE TABLE " + tableName);
                if(tableName.equals("MENU") || tableName.equals("MENU_GROUP") ||
                        tableName.equals("ORDER") || tableName.equals("ORDER_TABLE") ||
                        tableName.equals("PRODUCT") || tableName.equals("TABLE_GROUP")){
                    jdbcTemplate.execute("ALTER TABLE " + tableName + " ALTER COLUMN ID RESTART WITH 1");
                }
            }
        } finally {
            jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
        }
    }

    private static List<String> getTableNames(JdbcTemplate jdbcTemplate) {
        return (List<String>)jdbcTemplate.execute((ConnectionCallback) conn -> {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
            List<String> tableNames = new ArrayList<>();

            try {
                while(tables.next()) {
                    if(tables.getString("TABLE_NAME").equals("flyway_schema_history")){continue;}
                    tableNames.add(tables.getString("TABLE_NAME"));
                }
            } finally {
                tables.close();
            }

            return tableNames;
        });
    }
}
