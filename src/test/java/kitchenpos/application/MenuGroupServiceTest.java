package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.generator.MenuGroupGenerator;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.ui.request.MenuGroupRequest;
import kitchenpos.menu.ui.response.MenuGroupResponse;

@DisplayName("메뉴 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

	@Mock
	private MenuGroupDao menuGroupDao;

	@InjectMocks
	private MenuGroupService menuGroupService;

	@DisplayName("메뉴 그룹을 등록할 수 있다.")
	@Test
	void createMenuGroupTest() {
		// given
		MenuGroupRequest request = new MenuGroupRequest("한마리메뉴");
		given(menuGroupDao.save(any())).willReturn(MenuGroupGenerator.메뉴_그룹("한마리메뉴"));

		// when
		MenuGroupResponse response = menuGroupService.create(request);

		// then
		verify(menuGroupDao, only()).save(any());
		assertAll(
			() -> assertThat(response.getName()).isEqualTo(request.getName()),
			() -> assertThat(response.getId()).isNotNull()
		);
	}

	@DisplayName("메뉴 그룹들을 조회할 수 있다.")
	@Test
	void listMenuGroupTest() {
		// when
		menuGroupService.list();

		// then
		verify(menuGroupDao, only()).findAll();
	}

}
