package kitchenpos.utils;

import com.google.common.base.CaseFormat;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Service
@ActiveProfiles("test")
public class DatabaseCleanup implements InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        tableNames = entityManager.getMetamodel().getEntities().stream()
                .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
                .map(e -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName()))
                .map(changeOrderTableName())
                .collect(Collectors.toList());
    }

    private Function<String, String> changeOrderTableName(){
        return (name) -> {
            if (name.equals("order")) {
                return "orders";
            }
            return name;
        };
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (String tableName : tableNames) {
            System.out.println(tableName);
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            resetAutoIncrement(tableName);
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    private void resetAutoIncrement(String tableName) {
        if (IsIdColumnNameIsSeqTables(tableName)) {
            entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN SEQ RESTART WITH 1").executeUpdate();
            return;
        }
        entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN ID RESTART WITH 1").executeUpdate();
    }

    private boolean IsIdColumnNameIsSeqTables(String tableName) {
        return Objects.equals(tableName, "menu_product") || Objects.equals(tableName, "order_line_item");
    }
}