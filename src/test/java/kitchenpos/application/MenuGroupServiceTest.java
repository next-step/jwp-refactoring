package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

/**
 * @author : byungkyu
 * @date : 2021/01/19
 * @description :
 **/
@DisplayName("메뉴그룹")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
	@Mock
	private MenuGroupDao menuGroupDao;

	@DisplayName("메뉴 그룹을 등록할 수 있다.")
	@Test
	void create() {
		// given
		MenuGroup menuGroup = new MenuGroup("한식");

		MenuGroup expectedMenuGroup = mock(MenuGroup.class);
		when(expectedMenuGroup.getId()).thenReturn(1L);
		when(expectedMenuGroup.getName()).thenReturn("한식");

		when(menuGroupDao.save(menuGroup)).thenReturn(expectedMenuGroup);
		MenuGroupService menuGroupService = new MenuGroupService(menuGroupDao);

		// when
		MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

		// then
		assertThat(savedMenuGroup.getId()).isNotNull();
		assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName());
	}

	@DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
	@Test
	void list() {
		// given
		MenuGroup expectedMenuGroup = mock(MenuGroup.class);

		when(menuGroupDao.findAll()).thenReturn(Arrays.asList(expectedMenuGroup));
		MenuGroupService menuGroupService = new MenuGroupService(menuGroupDao);

		// when
		List<MenuGroup> menuGroups = menuGroupService.list();

		// then
		assertThat(menuGroups).containsExactly(expectedMenuGroup);
	}
}