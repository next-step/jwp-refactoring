package kitchenpos.acceptance;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseCleanUp implements InitializingBean {
    private static final Set<String> SEQ_TABLE_NAMES = new HashSet<>(Arrays.asList("ORDER_LINE_ITEM", "MENU_PRODUCT"));
    private static final String FLYWAY_TABLE_NAME = "FLYWAY_SCHEMA_HISTORY";
    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        entityManager.unwrap(Session.class)
                .doWork(this::extractTableNames);
    }

    private void extractTableNames(Connection conn) throws SQLException {
        tableNames = new ArrayList<>();
        ResultSet tables = conn
                .getMetaData()
                .getTables(conn.getCatalog(), null, "%", new String[]{"TABLE"});

        while (tables.next()) {
            tableNames.add(tables.getString("table_name").toUpperCase());
        }
    }

    @Transactional
    public void truncateAll() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        tableNames.remove(FLYWAY_TABLE_NAME);
        for (String tableName : tableNames) {
            String restartColumnName = getRestartColumnName(tableName);
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN " + restartColumnName + " RESTART WITH 1")
                    .executeUpdate();
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    private String getRestartColumnName(String tableName) {
        if (SEQ_TABLE_NAMES.contains(tableName)) {
            return "SEQ";
        }
        return "ID";
    }
}
