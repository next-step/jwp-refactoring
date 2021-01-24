package kitchenpos.ui;

import static kitchenpos.TestFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@SpringBootTest
@Sql({"/cleanup.sql", "/db/migration/V1__Initialize_project_tables.sql", "/db/migration/V2__Insert_default_data.sql"})
class MenuRestControllerTest {

	@Autowired
	MenuRestController menuRestController;

	@Test
	void create() {
		// given
		Menu 새_메뉴 = new Menu.Builder()
			.name("새_메뉴")
			.price(BigDecimal.valueOf(20000L))
			.menuGroup(메뉴_그룹1)
			.menuProducts(메뉴_상품1, 메뉴_상품2)
			.build();

		// when
		Menu createdMenu = menuRestController.create(새_메뉴).getBody();

		// then
		assertAll(
			() -> assertThat(createdMenu.getName()).isEqualTo("새_메뉴"),
			() -> assertThat(createdMenu.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(20000L)),
			() -> assertThat(createdMenu.getMenuProducts())
				.map(MenuProduct::getProduct)
				.map(Product::getName)
				.contains(상품1.getName(), 상품2.getName())
		);
	}

	@Test
	void list() {
		// given
		// @see V2__Insert_default_data.sql

		// when
		List<Menu> menuList = menuRestController.list().getBody();

		// then
		assertThat(menuList)
			.hasSize(6)
			.map(Menu::getName)
			.contains("후라이드치킨", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨");
	}
}
