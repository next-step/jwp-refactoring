package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupDao;

@DisplayName("메뉴 그룹 : 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

	@Mock
	MenuGroupDao menuGroupDao;

	@InjectMocks
	private MenuGroupService menuGroupService;

	private MenuGroup menuGroup;

	@BeforeEach
	void setup() {
		menuGroup = new MenuGroup();
	}

	@DisplayName("메뉴 그룹 생성 테스트")
	@Test
	void createMenuGroupTest() {
		// given
		menuGroup.setId(1L);
		menuGroup.setName("두마리메뉴");

		// when
		when(menuGroupDao.save(menuGroup)).thenReturn(menuGroup);

		// then
		assertThat(menuGroupService.create(menuGroup)).isEqualTo(menuGroup);
	}

	@DisplayName("메뉴 그룹 목록 조회 테스트")
	@Test
	void getList() {
		// given
		menuGroup.setId(1L);
		menuGroup.setName("두마리메뉴");
		MenuGroup anotherMenuGroup = new MenuGroup();
		anotherMenuGroup.setId(2L);
		anotherMenuGroup.setName("한마리메뉴");

		// when
		when(menuGroupDao.findAll()).thenReturn(Arrays.asList(menuGroup, anotherMenuGroup));

		// then
		assertThat(menuGroupService.list()).containsExactly(menuGroup, anotherMenuGroup);
	}
}
