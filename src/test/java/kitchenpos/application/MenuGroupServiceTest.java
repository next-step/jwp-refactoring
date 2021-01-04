package kitchenpos.application;

import static kitchenpos.common.TestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.BaseTest;
import kitchenpos.common.TestDataUtil;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@DisplayName("MenuGroupService 테스트")
class MenuGroupServiceTest extends BaseTest {

	@Autowired
	private MenuGroupService menuGroupService;

	@Autowired
	private MenuGroupDao menuGroupDao;

	@DisplayName("메뉴 그룹을 등록할 수 있다.")
	@Test
	void create() {
		MenuGroup menuGroup = menuGroupService.create(TestDataUtil.createMenuGroup(예제_메뉴그룹명));

		MenuGroup savedMenuGroup = menuGroupDao.findById(menuGroup.getId()).orElse(null);

		assertAll(
			() -> assertThat(savedMenuGroup.getId()).isNotNull(),
			() -> assertThat(savedMenuGroup.getName()).isEqualTo(예제_메뉴그룹명)
		);

	}

	@DisplayName("메뉴그룹을 조회할 수 있다.")
	@Test
	void list() {
		List<MenuGroup> menuGroups = menuGroupService.list();

		assertThat(menuGroups).hasSize(4);
	}

}