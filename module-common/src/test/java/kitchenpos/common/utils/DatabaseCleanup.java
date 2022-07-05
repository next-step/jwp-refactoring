package kitchenpos.common.utils;

import com.google.common.base.CaseFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Service
@ActiveProfiles("test")
public class DatabaseCleanup implements InitializingBean {
    private static final Set<String> SEQ_TABLE_SET = new HashSet<>(Arrays.asList("ORDER_LINE_ITEM", "MENU_PRODUCT"));

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        tableNames = extractTableNamesByEntityAnnotation();
        // Entity Annotation 없어서 비어있는 경우 fix
        if (tableNames.isEmpty()) {
            tableNames = fixKnownTables();
        }
    }

    private List<String> extractTableNamesByEntityAnnotation() {
        return entityManager.getMetamodel().getEntities().stream()
                .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
                .map(e -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE " + tableName +
                    " ALTER COLUMN " + getColumnNameSeqOrId(tableName) + " RESTART WITH 1").executeUpdate();
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    private String getColumnNameSeqOrId(String tableName) {
        tableName = tableName.toUpperCase();
        if (SEQ_TABLE_SET.contains(tableName)) {
            return "SEQ";
        }
        return "ID";
    }

    private List<String> fixKnownTables() {
        return Arrays.asList(
                "ORDERS", "ORDER_LINE_ITEM", "MENU",
                "MENU_GROUP", "MENU_PRODUCT", "ORDER_TABLE",
                "TABLE_GROUP", "PRODUCT"
        );
    }
}
