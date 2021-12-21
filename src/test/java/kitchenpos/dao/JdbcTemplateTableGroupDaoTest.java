package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import kitchenpos.domain.TableGroup;

class JdbcTemplateTableGroupDaoTest {

	private static DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
		.addScript("classpath:db/migration/V1__Initialize_project_tables.sql")
		.addScript("classpath:db/migration/V2__Insert_default_data.sql")
		.build();

	private static JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao = new JdbcTemplateTableGroupDao(dataSource);

	@Test
	@DisplayName("테이블그룹 저장 테스트")
	public void saveTableGroupTest() {
		//given
		TableGroup tableGroup = new TableGroup(null, LocalDateTime.now(), null);

		//when
		TableGroup save = jdbcTemplateTableGroupDao.save(tableGroup);

		//then
		assertThat(save).isNotNull();
		assertThat(save.getId()).isEqualTo(2L);
	}

	@Test
	@DisplayName("테이블그룹 목록 조회 테스트")
	public void findAllTableGroupTest() {
		//given

		//when
		List<TableGroup> tableGroups = jdbcTemplateTableGroupDao.findAll();

		//then
		assertThat(tableGroups).hasSizeGreaterThanOrEqualTo(1);
	}

	@Test
	@DisplayName("테이블그룹 id로 조회 테스트")
	public void findByIdTableGroupTest() {
		//given

		//when
		TableGroup tableGroup = jdbcTemplateTableGroupDao.findById(1L).orElse(new TableGroup());

		//then
		assertThat(tableGroup.getId()).isEqualTo(1L);
	}

	@Test
	@DisplayName("테이블그룹 없는 id로 조회 테스트")
	public void findByIdTableGroupFailTest() {
		//given

		//when
		TableGroup tableGroup = jdbcTemplateTableGroupDao.findById(9L).orElse(new TableGroup());

		//then
		assertThat(tableGroup.getId()).isNull();
	}
}
