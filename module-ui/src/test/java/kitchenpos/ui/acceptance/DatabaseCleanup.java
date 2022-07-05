package kitchenpos.ui.acceptance;


import com.google.common.base.CaseFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
    @PersistenceContext
    private EntityManager entityManager;

    private List<String> seqTables = Arrays.asList("menu_product", "order_line_item");

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        tableNames = entityManager.getMetamodel().getEntities().stream()
                .filter(e -> e.getJavaType().getAnnotation(Table.class) != null)
                .map(e -> {
                    Table annotation = e.getJavaType().getAnnotation(Table.class);
                    return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, annotation.name());
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            executeAlterWithPkSeqTable(tableName);
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    private void executeAlterWithPkSeqTable(String tableName) {
        String sql =
                seqTables.contains(tableName) ?
                        "ALTER TABLE " + tableName + " ALTER COLUMN SEQ RESTART WITH 1" :
                        "ALTER TABLE " + tableName + " ALTER COLUMN ID RESTART WITH 1";

        entityManager.createNativeQuery(sql).executeUpdate();
    }
}
