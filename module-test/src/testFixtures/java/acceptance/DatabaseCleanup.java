package acceptance;

import com.google.common.base.CaseFormat;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@ActiveProfiles("test")
public class DatabaseCleanup implements InitializingBean {
    private final static List<String> seqTables = Arrays.asList("menu_product", "order_line_item");

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNamesDefaultWithId;
    private List<String> tableNamesUserDefinedWithId;
    private List<String> tableNamesWithSeq;

    @Override
    public void afterPropertiesSet() {
        tableNamesDefaultWithId = entityManager.getMetamodel().getEntities().stream()
                .filter(e -> e.getJavaType().getAnnotation(Table.class) == null)
                .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
                .map(e -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName()))
                .filter(name -> !seqTables.stream().anyMatch(s -> s.equals(name)))
                .collect(Collectors.toList());

        tableNamesUserDefinedWithId = entityManager.getMetamodel().getEntities().stream()
                .filter(e -> e.getJavaType().getAnnotation(Table.class) != null)
                .map(e -> CaseFormat.UPPER_CAMEL.to(
                        CaseFormat.LOWER_UNDERSCORE, e.getJavaType().getAnnotation(Table.class).name())
                )
                .filter(name -> !seqTables.stream().anyMatch(s -> s.equals(name)))
                .collect(Collectors.toList());

        tableNamesWithSeq = entityManager.getMetamodel().getEntities().stream()
                .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
                .map(e -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName()))
                .filter(name -> seqTables.stream().anyMatch(s -> s.equals(name)))
                .collect(Collectors.toList());
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (String tableName : tableNamesDefaultWithId) {
            cleanupIds(tableName);
        }

        for (String tableName : tableNamesUserDefinedWithId) {
            cleanupIds(tableName);
        }

        for (String tableName : tableNamesWithSeq) {
            cleanupSeqs(tableName);
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    private void cleanupIds(String tableName) {
        entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN ID RESTART WITH 1")
                .executeUpdate();
    }

    private void cleanupSeqs(String tableName) {
        entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN SEQ RESTART WITH 1")
                .executeUpdate();
    }
}