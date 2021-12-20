package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import javax.sql.DataSource;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.annotation.Rollback;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

@Rollback
class JdbcTemplateMenuDaoTest {

	private static DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
		.addScript("classpath:db/migration/V1__Initialize_project_tables.sql")
		.addScript("classpath:db/migration/V2__Insert_default_data.sql")
		.build();

	private static JdbcTemplateMenuDao jdbcTemplateMenuDao = new JdbcTemplateMenuDao(dataSource);

	@Test
	@DisplayName("메뉴 저장 테스트")
	public void saveMenuTest() {
		//given
		//when
		Menu menu = new Menu(1L, "후라이드+후라이드", new BigDecimal(19000), 1L, null);

		//when
		Menu save = jdbcTemplateMenuDao.save(menu);

		//then
		assertThat(save).isNotNull();
		assertThat(save.getId()).isEqualTo(7L);
		assertThat(save.getName()).isEqualTo("후라이드+후라이드");
	}

	@Test
	@DisplayName("메뉴 목록 조회 테스트")
	public void findAllMenuTest() {
		//given

		//when
		List<Menu> menus = jdbcTemplateMenuDao.findAll();

		//then
		assertThat(menus).hasSizeGreaterThanOrEqualTo(6);
	}

	@Test
	@DisplayName("메뉴 id로 조회 테스트")
	public void findByIdMenuTest() {
		//given

		//when
		Menu menu = jdbcTemplateMenuDao.findById(6L).orElse(new Menu());

		//then
		assertThat(menu.getName()).isEqualTo("순살치킨");
	}

	@Test
	@DisplayName("없는 메뉴 id로 조회 테스트")
	public void findByIdMenuFailTest() {
		//given

		//when
		Menu menu = jdbcTemplateMenuDao.findById(8L).orElse(new Menu());

		//then
		assertThat(menu.getId()).isNull();
	}

	@Test
	@DisplayName("메뉴 id 여러개로 갯수 조회 테스트")
	public void countByIdInTest() {
		//given

		//when
		long count = jdbcTemplateMenuDao.countByIdIn(Lists.newArrayList(1L, 2L, 3L, 10L, 11L));

		//then
		assertThat(count).isEqualTo(3);
	}


}
