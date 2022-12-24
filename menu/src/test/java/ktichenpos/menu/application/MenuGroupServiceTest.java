package ktichenpos.menu.application;

import static ktichenpos.menu.generator.MenuGroupGenerator.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ktichenpos.menu.menu.application.MenuGroupService;
import ktichenpos.menu.menu.domain.MenuGroup;
import ktichenpos.menu.menu.domain.MenuGroupRepository;
import ktichenpos.menu.menu.ui.request.MenuGroupRequest;
import ktichenpos.menu.menu.ui.response.MenuGroupResponse;

@DisplayName("메뉴 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

	@Mock
	private MenuGroupRepository menuGroupRepository;

	@InjectMocks
	private MenuGroupService menuGroupService;

	@DisplayName("메뉴 그룹을 등록할 수 있다.")
	@Test
	void createMenuGroupTest() {
		// given
		MenuGroupRequest request = new MenuGroupRequest("한마리 메뉴");
		MenuGroup 한마리_메뉴 = 한마리_메뉴();
		given(menuGroupRepository.save(any())).willReturn(한마리_메뉴);

		// when
		MenuGroupResponse response = menuGroupService.create(request);

		// then
		verify(menuGroupRepository, only()).save(any());
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
		verify(menuGroupRepository, only()).findAll();
	}

}
