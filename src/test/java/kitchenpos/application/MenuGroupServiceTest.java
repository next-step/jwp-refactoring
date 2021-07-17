package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menuGroup.application.MenuGroupService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.menuGroup.dto.MenuGroupRequest;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
	private MenuGroupService menuGroupService;

	@Mock
	private MenuGroupDao menuGroupDao;

	@BeforeEach
	void setUp() {
		menuGroupService = new MenuGroupService(menuGroupDao);
	}

	@DisplayName("메뉴 그룹을 등록한다.")
	@Test
	void createMenuGroup() {
		// given
		when(menuGroupDao.save(any())).thenReturn(new MenuGroupRequest("메뉴 그룹 1번"));
		// when
		MenuGroupRequest menuGroup = menuGroupService.create(new MenuGroupRequest());
		// then
		assertThat(menuGroup.getName()).isEqualTo("메뉴 그룹 1번");
	}

	@DisplayName("메뉴 그룹을 조회한다.")
	@Test
	void findMenuGroup() {
		// given
		when(menuGroupDao.findAll()).thenReturn(Arrays.asList(new MenuGroupRequest("메뉴 그룹 1번"), new MenuGroupRequest("메뉴 그룹 2번")));
		// when
		List<MenuGroupRequest> menuGroups = menuGroupService.list();
		// then
		assertThat(menuGroups.size()).isEqualTo(2);
		assertThat(menuGroups.get(0).getName()).isEqualTo("메뉴 그룹 1번");
		assertThat(menuGroups.get(1).getName()).isEqualTo("메뉴 그룹 2번");
	}
}
