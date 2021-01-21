package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.MenuGroup;

@SpringBootTest
@Sql({"/cleanup.sql", "/db/migration/V1__Initialize_project_tables.sql", "/db/migration/V2__Insert_default_data.sql"})
class MenuGroupRestControllerTest {

	@Autowired
	MenuGroupRestController menuGroupRestController;

	@Test
	void create() {
		// given
		MenuGroup menuGroup = new MenuGroup();
		menuGroup.setName("메뉴_그룹");

		// when
		MenuGroup createdMenuGroup = menuGroupRestController.create(menuGroup).getBody();

		// then
		assertAll(
			() -> assertThat(createdMenuGroup.getName()).isEqualTo("메뉴_그룹")
		);
	}

	@Test
	void list() {
		// given
		MenuGroup menuGroup1 = new MenuGroup();
		menuGroup1.setName("메뉴_그룹1");
		menuGroupRestController.create(menuGroup1);
		MenuGroup menuGroup2 = new MenuGroup();
		menuGroup2.setName("메뉴_그룹2");
		menuGroupRestController.create(menuGroup2);

		// when
		List<MenuGroup> menuGroupList = menuGroupRestController.list().getBody();

		// then
		assertThat(menuGroupList).map(MenuGroup::getName).contains("메뉴_그룹1", "메뉴_그룹2");
	}
}
