package kitchenpos;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.CaseFormat;

@Service
@ActiveProfiles("test")
public class DatabaseCleanup implements InitializingBean {
    public static final String SEQ = "seq";
    public static final String ID = "id";
    public static final String MENU_PRODUCT_TABLE_NAME = "menu_product";
    public static final String ORDER_TABLE_NAME = "order";
    public static final String ORDERS_TABLE_NAME = "orders";
    public static final String ORDER_LINE_ITEM_TABLE_NAME = "order_line_item";

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        tableNames = entityManager.getMetamodel().getEntities().stream()
                .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
                .map(e -> CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (String tableName : tableNames) {
            String modifiedTableName = getTableName(tableName);
            String id = getPkColumnName(modifiedTableName);
            entityManager.createNativeQuery("TRUNCATE TABLE " + modifiedTableName).executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE " + modifiedTableName + " ALTER COLUMN "+ id + " RESTART WITH 1").executeUpdate();
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    private String getTableName(String tableName) {
        if (tableName.equals(ORDER_TABLE_NAME)) {
            return ORDERS_TABLE_NAME;
        }
        return tableName;
    }

    private String getPkColumnName(String tableName) {
        if (tableName.equals(MENU_PRODUCT_TABLE_NAME) || tableName.equals(ORDER_LINE_ITEM_TABLE_NAME)) {
            return SEQ;
        }
        return ID;
    }
}