package kitchenpos.ui;

import static kitchenpos.TestFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.MenuRestController;

@SpringBootTest
@Sql({"/cleanup.sql", "/db/migration/V1__Initialize_project_tables.sql", "/db/migration/V2__Insert_default_data.sql",
	"/db/migration/V3__remove_two_way.sql"})
class MenuRestControllerTest {

	@Autowired
	MenuRestController menuRestController;

	@Test
	void create() {
		// given
		MenuRequest 새_메뉴_요청 = new MenuRequest(
			"새_메뉴",
			BigDecimal.valueOf(20000L),
			메뉴_그룹1.getId(),
			Arrays.asList(
				new MenuProductRequest(상품1.getId(), 1L),
				new MenuProductRequest(상품2.getId(), 2L)
			)
		);

		// when
		MenuResponse response = menuRestController.create(새_메뉴_요청).getBody();

		// then
		assertAll(
			() -> assertThat(response.getName()).isEqualTo("새_메뉴"),
			() -> assertThat(response.getPrice()).isEqualTo(20000L),
			() -> assertThat(response.getMenuProducts())
				.map(MenuProductResponse::getProductId)
				.contains(상품1.getId(), 상품2.getId())
		);
	}

	@Test
	void list() {
		// given
		// @see V2__Insert_default_data.sql

		// when
		List<MenuResponse> menuList = menuRestController.list().getBody();

		// then
		assertThat(menuList)
			.hasSize(6)
			.map(MenuResponse::getName)
			.contains("후라이드치킨", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨");
	}
}
