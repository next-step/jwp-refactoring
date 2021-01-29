package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.ui.MenuGroupRestController;

@SpringBootTest
@Sql({"/cleanup.sql", "/db/migration/V1__Initialize_project_tables.sql", "/db/migration/V2__Insert_default_data.sql",
	"/db/migration/V3__remove_two_way.sql"})
class MenuGroupRestControllerTest {

	@Autowired
	MenuGroupRestController menuGroupRestController;

	@Test
	void create() {
		// given
		MenuGroupRequest request = new MenuGroupRequest("메뉴_그룹");

		// when
		MenuGroupResponse response = menuGroupRestController.create(request).getBody();

		// then
		assertAll(
			() -> assertThat(response.getName()).isEqualTo("메뉴_그룹")
		);
	}

	@Test
	void list() {
		// when
		List<MenuGroupResponse> menuGroupList = menuGroupRestController.list().getBody();

		// then
		assertThat(menuGroupList).map(MenuGroupResponse::getName).contains("두마리메뉴", "한마리메뉴", "순살파닭두마리메뉴", "신메뉴");
	}
}
