package kitchenpos.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

@Service
@ActiveProfiles("test")
public class DatabaseCleanup implements InitializingBean {
    @PersistenceContext
    private EntityManager entityManager;

    private Map<String, String> tableNames;

    @Override
    public void afterPropertiesSet() {
        tableNames = new HashMap<>();
        tableNames.put("orders", "id");
        tableNames.put("order_line_item", "seq");
        tableNames.put("menu", "id");
        tableNames.put("menu_group", "id");
        tableNames.put("menu_product", "seq");
        tableNames.put("order_table", "id");
        tableNames.put("table_group", "id");
        tableNames.put("product", "id");
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (Map.Entry<String, String> entry : tableNames.entrySet()) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + entry.getKey()).executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE " + entry.getKey() + " ALTER COLUMN " + entry.getValue() + " RESTART WITH 1")
                    .executeUpdate();
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

}
