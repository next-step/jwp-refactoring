package kitchenpos.application.mock;

import static kitchenpos.domain.DomainFactory.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.MenuGroupService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
	@InjectMocks
	private MenuGroupService menuGroupService;
	@Mock
	private MenuGroupDao menuGroupDao;

	@DisplayName("메뉴 그룹 등록")
	@Test
	void create() {
		String name = "추천메뉴";
		when(menuGroupDao.save(any(MenuGroup.class))).thenAnswer(invocation -> {
			MenuGroup menuGroup = invocation.getArgument(0, MenuGroup.class);
			menuGroup.setId(1L);
			return menuGroup;
		});

		MenuGroup resultGroup = menuGroupService.create(createMenuGroup(name));

		assertThat(resultGroup.getId()).isNotNull();
		assertThat(resultGroup.getName()).isEqualTo(name);
	}

	@DisplayName("메뉴 그룹 목록 조회")
	@Test
	void list() {
		when(menuGroupDao.findAll()).thenReturn(Arrays.asList(
			createMenuGroup(1L, "추천메뉴"),
			createMenuGroup(2L, "대표메뉴"),
			createMenuGroup(3L, "할인메뉴")
		));

		List<MenuGroup> menuGroups = menuGroupService.list();

		assertThat(menuGroups).hasSize(3);
	}
}
