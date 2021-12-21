package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import javax.sql.DataSource;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.annotation.Rollback;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

class JdbcTemplateMenuProductDaoTest {

	private static DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
		.addScript("classpath:db/migration/V1__Initialize_project_tables.sql")
		.addScript("classpath:db/migration/V2__Insert_default_data.sql")
		.build();

	private static JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao = new JdbcTemplateMenuProductDao(dataSource);

	@Test
	@DisplayName("메뉴상품 저장 테스트")
	public void saveMenuProductTest() {
		//given
		//when
		MenuProduct menuProduct = new MenuProduct(null, 2L, 1L, 2);

		//when
		MenuProduct save = jdbcTemplateMenuProductDao.save(menuProduct);

		//then
		assertThat(save).isNotNull();
		assertThat(save.getSeq()).isEqualTo(7L);
	}

	@Test
	@DisplayName("메뉴상품 목록 조회 테스트")
	public void findAllMenuProductTest() {
		//given

		//when
		List<MenuProduct> menus = jdbcTemplateMenuProductDao.findAll();

		//then
		assertThat(menus).hasSizeGreaterThanOrEqualTo(6);
	}

	@Test
	@DisplayName("메뉴상품 id로 조회 테스트")
	public void findByIdMenuProductTest() {
		//given

		//when
		MenuProduct menu = jdbcTemplateMenuProductDao.findById(6L).orElse(new MenuProduct());

		//then
		assertThat(menu.getMenuId()).isEqualTo(6L);
	}

	@Test
	@DisplayName("없는 메뉴 id로 조회 테스트")
	public void findByIdMenuProductFailTest() {
		//given

		//when
		MenuProduct menu = jdbcTemplateMenuProductDao.findById(8L).orElse(new MenuProduct());

		//then
		assertThat(menu.getMenuId()).isNull();
	}

	@Test
	@DisplayName("메뉴 id로 메뉴상품 조회 테스트")
	public void findAllByMenuIdTest() {
		//given

		//when
		List<MenuProduct> menuProducts = jdbcTemplateMenuProductDao.findAllByMenuId(2L);

		//then
		assertThat(menuProducts).hasSize(1);
	}


}
