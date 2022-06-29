package kitchenpos.utils;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@ActiveProfiles("test")
public class DatabaseCleanup implements InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;

    private final Map<String, String> specificTableNames = ImmutableMap.<String, String>builder()
                                                                       .put("order_line_item", "seq")
                                                                       .put("menu_product", "seq")
                                                                       .build();
    private List<String> tableNames;

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (String tableName : tableNames) {
            String column = specificTableNames.getOrDefault(tableName, "id");
            executeTruncateTable(tableName);
            executeResetAutoIncrement(tableName, column);
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    private void executeTruncateTable(String tableName) {
        entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
    }

    private void executeResetAutoIncrement(String tableName, String column) {
        entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN " + column + " RESTART WITH 1")
                     .executeUpdate();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        tableNames = entityManager.getMetamodel()
                                  .getEntities()
                                  .stream()
                                  .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
                                  .map(e -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName()))
                                  .collect(Collectors.toList());
    }
}
