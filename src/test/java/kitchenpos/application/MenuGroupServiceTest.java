package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
	private MenuGroupDao menuGroupDao;

	@InjectMocks
	private MenuGroupService menuGroupService;

	@DisplayName("메뉴 그룹 생성 테스트")
	@Test
	void testCreateMenuGroup() {
		MenuGroup menuGroup = mock(MenuGroup.class);

		MenuGroup expected = new MenuGroup(1L, "menuGroup");
		when(menuGroupDao.save(eq(menuGroup))).thenReturn(expected);

		MenuGroup actual = menuGroupService.create(menuGroup);

		verify(menuGroupDao, times(1)).save(eq(menuGroup));
		assertThat(actual.getId()).isEqualTo(expected.getId());
		assertThat(actual.getName()).isEqualTo(expected.getName());
	}

	@DisplayName("메뉴 그룹 목록 조회 테스트")
	@Test
	void testList() {
		List<MenuGroup> expected = new ArrayList<>();
		expected.add(new MenuGroup(1L, "menuGroup1"));
		expected.add(new MenuGroup(2L, "menuGroup2"));
		expected.add(new MenuGroup(3L, "menuGroup3"));
		List<Long> savedMenuGroupIds = expected.stream().map(MenuGroup::getId).collect(Collectors.toList());

		when(menuGroupDao.findAll()).thenReturn(expected);

		List<MenuGroup> actual = menuGroupService.list();

		verify(menuGroupDao, times(1)).findAll();
		assertThat(actual.stream().map(MenuGroup::getId).collect(Collectors.toList()))
			.containsExactlyElementsOf(savedMenuGroupIds);
	}
}