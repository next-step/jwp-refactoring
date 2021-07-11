package kitchenpos.acceptance;

import com.google.common.base.CaseFormat;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Service
@ActiveProfiles("test")
public class DatabaseCleanUp implements InitializingBean {
    @PersistenceContext
    private EntityManager entityManager;

    private List<TableKey> tableKeys;

    @Override
    public void afterPropertiesSet() {
        tableKeys = entityManager.getMetamodel().getEntities().stream()
                                  .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
                                  .map(TableKey::of)
                                  .collect(Collectors.toList());
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (TableKey tableKey : tableKeys) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableKey.tableName).executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE " + tableKey.tableName
                                                + " ALTER COLUMN " + tableKey.primaryKeyName
                                                + " RESTART WITH 1").executeUpdate();
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }


    private static class TableKey {
        private String tableName;
        private String primaryKeyName;

        private TableKey(final String tableName, final String primaryKeyName) {
            this.tableName = tableName;
            this.primaryKeyName = primaryKeyName;
        }

        public static TableKey of(EntityType entityType) {
            return new TableKey(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entityType.getName()),
                                entityType.getId(entityType.getIdType().getJavaType()).getName());
        }
    }
}
