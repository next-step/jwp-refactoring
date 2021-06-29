package kitchenpos.application;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@DisplayName("메뉴 그룹 로직 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

	@Mock
	private MenuGroupDao menuGroupDao;

	@InjectMocks
	private MenuGroupService menuGroupService;


	@DisplayName("메뉴 그룹을 등록할 수 있다.")
	@Test
	void createTest() {
		// given
		MenuGroup menuGroup = mock(MenuGroup.class);

		// when
		menuGroupService.create(menuGroup);

		// than
		verify(menuGroupDao).save(menuGroup);
	}

	@DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
	@Test
	void listTest() {
		// given
		MenuGroup menuGroup = mock(MenuGroup.class);
		when(menuGroupDao.findAll()).thenReturn(asList(menuGroup));

		// when
		List<MenuGroup> menuGroups = menuGroupService.list();

		// then
		assertThat(menuGroups).containsExactly(menuGroup);
	}

}