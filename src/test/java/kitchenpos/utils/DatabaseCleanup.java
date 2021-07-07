package kitchenpos.utils;

import com.google.common.base.CaseFormat;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Service
@ActiveProfiles("test")
public class DatabaseCleanup implements InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;

    private List<TablePrimaryKey> tablePrimaryKeys;

    @Override
    public void afterPropertiesSet() {
        tablePrimaryKeys = entityManager.getMetamodel().getEntities().stream()
            .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
            .map(e -> {
                SingularAttribute<?, ?> id = e.getId(e.getIdType().getJavaType());
                return new TablePrimaryKey(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName()), id.getName());
            })
            .collect(Collectors.toList());
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (TablePrimaryKey tablePrimaryKey : tablePrimaryKeys) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tablePrimaryKey.getTableName()).executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE " + tablePrimaryKey.getTableName() + " ALTER COLUMN " + tablePrimaryKey.getPrimaryKey() + " RESTART WITH 1").executeUpdate();
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

}