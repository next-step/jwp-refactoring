package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@DisplayName("메뉴 그럽 Stubbing 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

	@Mock
	private MenuGroupDao menuGroupDao;

	private MenuGroupService menuGroupService;

	@BeforeEach
	void setUp() {
		menuGroupService = new MenuGroupService(menuGroupDao);
	}

	@DisplayName("메뉴 그룹: 메뉴 그룹 생성 테스트")
	@Test
	void createTest() {
		// given
		MenuGroup menuGroup = MenuGroup.of(1L, "두마리메뉴");
		given(menuGroupDao.save(any())).willReturn(menuGroup);

		// when
		MenuGroup actual = menuGroupService.create(menuGroup);

		// then
		assertThat(actual).isNotNull();
	}

	@DisplayName("메뉴 그룹: 메뉴 그룹 조회 테스트")
	@Test
	void listTest() {
		// given
		given(menuGroupDao.findAll()).willReturn(Arrays.asList(
			MenuGroup.of(1L, "두마리메뉴"),
			MenuGroup.of(2L, "세마리메뉴")
		));

		// when
		List<MenuGroup> actual = menuGroupService.list();

		// then
		assertThat(actual).hasSize(2);
	}
}
