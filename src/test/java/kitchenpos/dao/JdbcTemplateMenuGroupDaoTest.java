package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.annotation.Rollback;

import kitchenpos.domain.MenuGroup;

@Rollback
class JdbcTemplateMenuGroupDaoTest {
	private static final String NAME = "후라이드+양념";
	private static final String OTHER_NAME = "로제+양념";

	private static DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
		.addScript("classpath:db/migration/V1__Initialize_project_tables.sql")
		.addScript("classpath:db/migration/V2__Insert_default_data.sql")
		.build();

	private static JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);

	@Test
	@DisplayName("메뉴그룹 저장 테스트")
	public void saveMenuGroupTest() {
		//given
		MenuGroup menuGroup = new MenuGroup(null, NAME);

		//when
		MenuGroup save = jdbcTemplateMenuGroupDao.save(menuGroup);

		//then
		assertThat(save).isNotNull();
		assertThat(save.getId()).isEqualTo(5L);
		assertThat(save.getName()).isEqualTo(NAME);
	}

	@Test
	@DisplayName("메뉴그룹 목록 조회 테스트")
	public void findAllMenuGroupTest() {
		//given

		//when
		List<MenuGroup> menuGroups = jdbcTemplateMenuGroupDao.findAll();

		//then
		assertThat(menuGroups).hasSizeGreaterThanOrEqualTo(4);
	}

	@Test
	@DisplayName("메뉴그룹 id로 조회 테스트")
	public void findByIdMenuGroupTest() {
		//given

		//when
		MenuGroup menuGroup = jdbcTemplateMenuGroupDao.findById(3L).orElse(new MenuGroup());

		//then
		assertThat(menuGroup.getName()).isEqualTo("순살파닭두마리메뉴");
	}

	@Test
	@DisplayName("메뉴그룹 없는 id로 조회 테스트")
	public void findByIdMenuGroupFailTest() {
		//given

		//when
		MenuGroup menuGroup = jdbcTemplateMenuGroupDao.findById(9L).orElse(new MenuGroup());

		//then
		assertThat(menuGroup.getId()).isNull();
	}

	@ParameterizedTest
	@CsvSource(value = {"3:true", "9:false"}, delimiter = ':')
	@DisplayName("메뉴그룹 id가 존재하는지 여부 테스트")
	public void existsByIdTest(Long id, boolean expected) {
		//given

		//when
		boolean result = jdbcTemplateMenuGroupDao.existsById(id);

		//then
		assertThat(result).isEqualTo(expected);
	}


}
