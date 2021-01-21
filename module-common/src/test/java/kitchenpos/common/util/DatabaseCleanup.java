package kitchenpos.common.util;

import com.google.common.base.CaseFormat;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;

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
            .map(this::getTableName)
            .collect(Collectors.toList());
    }

    private String getTableName(final javax.persistence.metamodel.EntityType<?> entity) {
        final Table tableAnnotation = entity.getJavaType().getAnnotation(Table.class);
        if (tableAnnotation != null) {
            return parseToCaseFormat(tableAnnotation.name());
        }

        return parseToCaseFormat(entity.getName());
    }

    private String parseToCaseFormat(final String name) {
        return CaseFormat.UPPER_CAMEL
            .to(CaseFormat.LOWER_UNDERSCORE, name);
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName)
                .executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN ID RESTART WITH 1")
                .executeUpdate();
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE")
            .executeUpdate();
    }
}
