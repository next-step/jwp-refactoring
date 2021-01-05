package kitchenpos.application;

import static kitchenpos.common.TestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.BaseTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.repository.MenuGroupRepository;

@DisplayName("MenuGroupService 테스트")
class MenuGroupServiceTest extends BaseTest {

	@Autowired
	private MenuGroupService menuGroupService;

	@Autowired
	private MenuGroupRepository menuGroupRepository;

	@DisplayName("메뉴 그룹을 등록할 수 있다.")
	@Test
	void create() {
		MenuGroupResponse menuGroup = menuGroupService.create(MenuGroupRequest.of(예제_메뉴그룹명));

		MenuGroup savedMenuGroup = menuGroupRepository.findById(menuGroup.getId()).orElse(null);

		assertAll(
			() -> assertThat(savedMenuGroup.getId()).isNotNull(),
			() -> assertThat(savedMenuGroup.getName()).isEqualTo(예제_메뉴그룹명)
		);

	}

	@DisplayName("메뉴그룹을 조회할 수 있다.")
	@Test
	void list() {
		List<MenuGroupResponse> menuGroups = menuGroupService.list();

		assertThat(menuGroups).hasSize(4);
	}

}