package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

	@Mock
	MenuGroupDao menuGroupDao;
	MenuGroupService menuGroupService;

	@BeforeEach
	void setup() {
		menuGroupService = new MenuGroupService(menuGroupDao);
	}

	@Test
	void testCreateMenuGroup() {
		MenuGroup menuGroup = new MenuGroup();
		menuGroup.setName("메뉴그룹1");

		menuGroupService.create(menuGroup);

		verify(menuGroupDao, times(1)).save(any());
	}

	@Test
	void testMenuLst() {
		menuGroupService.list();

		verify(menuGroupDao, times(1)).findAll();
	}
}
