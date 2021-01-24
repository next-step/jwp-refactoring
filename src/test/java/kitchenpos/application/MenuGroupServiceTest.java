package kitchenpos.application;

import static kitchenpos.domain.DomainFactory.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import kitchenpos.domain.MenuGroup;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MenuGroupServiceTest {
	@Autowired
	private MenuGroupService menuGroupService;

	@DisplayName("메뉴 그룹 등록")
	@Test
	void create() {
		String name = "추천메뉴";
		MenuGroup menuGroup = createMenuGroup(name);

		MenuGroup resultGroup = menuGroupService.create(menuGroup);

		assertThat(resultGroup.getId()).isNotNull();
		assertThat(resultGroup.getName()).isEqualTo(name);
	}

	@DisplayName("메뉴 그룹 목록 조회")
	@Test
	void list() {
		List<MenuGroup> menuGroups = menuGroupService.list();

		assertThat(menuGroups).hasSize(4);
	}
}