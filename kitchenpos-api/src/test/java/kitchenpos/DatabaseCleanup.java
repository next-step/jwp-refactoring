package kitchenpos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

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

	private List<String> tableNames;
	private List<String> idNames = new ArrayList<>();

	@Override
	public void afterPropertiesSet() {
		tableNames = entityManager.getMetamodel().getEntities().stream()
			.filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
			.map(e -> {
				idNames.add(e.getId(e.getIdType().getJavaType()).getName());

				Table table = e.getJavaType().getAnnotation(Table.class);
				if (table != null) {
					return table.name();
				}
				return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName());
			})
			.collect(Collectors.toList());
	}

	@Transactional
	public void execute() {
		entityManager.flush();
		entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

		for (int i = 0; i < tableNames.size(); i++) {
			entityManager.createNativeQuery("TRUNCATE TABLE " + tableNames.get(i)).executeUpdate();
			entityManager.createNativeQuery(
				"ALTER TABLE " + tableNames.get(i) + " ALTER COLUMN " + idNames.get(i) + " RESTART WITH 1")
				.executeUpdate();
		}

		entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
	}
}
