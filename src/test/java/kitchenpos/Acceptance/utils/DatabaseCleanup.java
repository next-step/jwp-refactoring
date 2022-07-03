package kitchenpos.Acceptance.utils;

import com.google.common.base.CaseFormat;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DatabaseCleanup implements InitializingBean {
    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        tableNames = entityManager.getMetamodel().getEntities().stream()
                .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
                .map(e -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (String tableName : tableNames) {
            String convertedTableName = convertTableName(tableName);

            entityManager.createNativeQuery("TRUNCATE TABLE " + convertedTableName).executeUpdate();
            entityManager.createNativeQuery(
                    "ALTER TABLE " + convertedTableName +
                            " ALTER COLUMN " + primaryKeyColumnNameByTable(convertedTableName) +
                            " RESTART WITH 1")
                    .executeUpdate();
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    public String primaryKeyColumnNameByTable(String tableName) {
        if (seqPrimaryKeyTables().contains(tableName)) {
            return "seq";
        }
        return "id";
    }

    private String convertTableName(String tableName) {
        return customConvertTableTarget().getOrDefault(tableName, tableName);
    }

    private Map<String, String> customConvertTableTarget() {
        return Collections.singletonMap("order", "orders");
    }

    private List<String> seqPrimaryKeyTables() {
        return Arrays.asList("order_line_item", "menu_product");
    }
}
