package kitchenpos.utils;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.CaseFormat;

@Service
@ActiveProfiles("test")
public class DatabaseCleanup implements InitializingBean {
    @PersistenceContext
    private EntityManager entityManager;

    private List<TableInformation> tableInformationList;

    @Override
    public void afterPropertiesSet() {
        tableInformationList = entityManager.getMetamodel().getEntities().stream()
            .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
            .map(e -> {
                String name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName());
                List<String> idColumns = idColumns(e);
                return new TableInformation(name, idColumns);
            })
            .collect(Collectors.toList());
    }

    private List<String> idColumns(EntityType<?> e) {
        return e.getAttributes().stream()
            .filter(attribute -> attribute instanceof SingularAttribute)
            .filter(attribute -> ((SingularAttribute<?, ?>)attribute).isId())
            .map(Attribute::getName)
            .collect(Collectors.toList());
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (TableInformation tableInformation : tableInformationList) {
            truncateTable(tableInformation);
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    private void truncateTable(TableInformation tableInformation) {
        String tableName = tableInformation.getTableName();
        List<String> idColumnNames = tableInformation.getIdColumnNames();
        entityManager.createNativeQuery(String.format("TRUNCATE TABLE %s", tableName)).executeUpdate();
        for (String idColumnName : idColumnNames) {
            entityManager.createNativeQuery(
                String.format("ALTER TABLE %s ALTER COLUMN %s RESTART WITH 1", tableName, idColumnName)
            ).executeUpdate();
        }
    }
}
