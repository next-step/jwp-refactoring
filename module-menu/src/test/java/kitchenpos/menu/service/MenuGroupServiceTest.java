package kitchenpos.menu.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

	@Mock
	MenuGroupRepository menuGroupRepository;

	@InjectMocks
	MenuGroupService menuGroupService;

	@Test
	@DisplayName("메뉴 그룹 생성")
	void testCreateMenuGroup() {
		MenuGroup menuGroup = createMenuGroup();

		menuGroupService.create(menuGroup);

		verify(menuGroupRepository, times(1)).save(menuGroup);
	}

	@Test
	@DisplayName("메뉴 목록 조회")
	void testMenuLst() {
		menuGroupService.findAll();

		verify(menuGroupRepository, times(1)).findAll();
	}

	private MenuGroup createMenuGroup() {
		return new MenuGroup("menu-group");
	}

}
