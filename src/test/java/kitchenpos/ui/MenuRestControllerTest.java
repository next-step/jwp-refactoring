package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

@SpringBootTest
@Sql({"/cleanup.sql", "/db/migration/V1__Initialize_project_tables.sql", "/db/migration/V2__Insert_default_data.sql"})
class MenuRestControllerTest {

	@Autowired
	MenuRestController menuRestController;

	@Test
	void create() {
		// given
		MenuProduct 메뉴_상품1 = new MenuProduct();
		메뉴_상품1.setProductId(1L);
		메뉴_상품1.setQuantity(3L);
		MenuProduct 메뉴_상품2 = new MenuProduct();
		메뉴_상품2.setProductId(2L);
		메뉴_상품2.setQuantity(2L);
		Menu 메뉴 = new Menu();
		메뉴.setName("메뉴");
		메뉴.setMenuGroupId(4L);
		메뉴.setPrice(BigDecimal.valueOf(20000L));
		메뉴.setMenuProducts(Arrays.asList(메뉴_상품1, 메뉴_상품2));

		// when
		Menu createdMenu = menuRestController.create(메뉴).getBody();

		// then
		assertAll(
			() -> assertThat(createdMenu.getName()).isEqualTo("메뉴"),
			() -> assertThat(createdMenu.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(20000L)),
			() -> assertThat(createdMenu.getMenuProducts()).map(MenuProduct::getProductId).contains(1L, 2L)
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
