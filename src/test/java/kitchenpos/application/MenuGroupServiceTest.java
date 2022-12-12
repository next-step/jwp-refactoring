package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

	@Mock
	MenuGroupDao menuGroupDao;
	@InjectMocks
	MenuGroupService menuGroupService;

	@Test
	@DisplayName("메뉴 그룹 생성")
	void testCreateMenuGroup() {
		MenuGroup menuGroup = new MenuGroup();
		menuGroup.setName("메뉴그룹1");

		menuGroupService.create(menuGroup);

		verify(menuGroupDao, times(1)).save(any());
	}

	@Test
	@DisplayName("메뉴 목록 조회")
	void testMenuLst() {
		menuGroupService.list();

		verify(menuGroupDao, times(1)).findAll();
	}

	public static MenuGroup createMenuGroup() {
		MenuGroup menuGroup = new MenuGroup();
		menuGroup.setId(1L);
		menuGroup.setName("베이커리");
		return menuGroup;
	}
}
