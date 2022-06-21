package kitchenpos.util;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseCleanup {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional
    public void execute() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");

        try {
            for (String tableName : getTableNames()) {
                jdbcTemplate.execute("TRUNCATE TABLE " + tableName);
            }
        } finally {
            jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
        }
    }

    private List<String> getTableNames() {
        ConnectionCallback table_name = conn -> {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
            ArrayList<Object> tableNames = Lists.newArrayList();

            try {
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    if (tableName.equals("flyway_schema_history")) {
                        continue;
                    }
                    tableNames.add(tableName);
                }
            } finally {
                tables.close();
            }

            return tableNames;
        };

        return (List<String>) this.jdbcTemplate.execute(table_name);
    }

}
