package kitchenpos.utils;

import com.google.common.base.CaseFormat;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseCleanup implements InitializingBean {
    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    private static final List<String> tableNamesByPkNameIsSEQ = Lists.newArrayList(
            "menu_product",
            "order_line_item"
    );

    @Override
    public void afterPropertiesSet() {

        tableNames = entityManager.getMetamodel().getEntities().stream()
                .filter(e -> e.getJavaType().getAnnotation(Table.class) != null)
                .map(e -> {
                    Table tableAnnotation = e.getJavaType().getAnnotation(Table.class);
                    return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, tableAnnotation.name());
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (String tableName : findTableNamesByPkNameIsID()) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN ID RESTART WITH 1").executeUpdate();
        }

        for (String tableName : findTableNamesByPkNameIsSEQ()) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE " + tableName + "  ALTER COLUMN SEQ RESTART WITH 1").executeUpdate();
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    private List<String> findTableNamesByPkNameIsID() {
        return tableNames.stream()
                .filter(tableName -> !tableNamesByPkNameIsSEQ.contains(tableName))
                .collect(Collectors.toList());
    }

    private List<String> findTableNamesByPkNameIsSEQ() {
        return tableNames.stream()
                .filter(tableNamesByPkNameIsSEQ::contains)
                .collect(Collectors.toList());
    }
}