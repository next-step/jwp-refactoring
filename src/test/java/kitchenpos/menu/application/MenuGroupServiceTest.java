package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;

@DisplayName("메뉴 그룹 : 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

	@Mock
	MenuGroupRepository menuGroupRepository;

	@InjectMocks
	private MenuGroupService menuGroupService;

	private MenuGroup menuGroup;

	@DisplayName("메뉴 그룹 생성 테스트")
	@Test
	void createMenuGroupTest() {
		// given
		MenuGroupRequest menuGroupRequest = MenuGroupRequest.from("불닭 그룹");

		// when
		when(menuGroupRepository.save(any(MenuGroup.class))).thenReturn(menuGroup);

		// then
		assertThat(menuGroupService.create(menuGroupRequest)).isEqualTo(menuGroup);
	}

	@DisplayName("메뉴 그룹 목록 조회 테스트")
	@Test
	void getList() {
		// given
		menuGroup = MenuGroup.from("두마리 메뉴");
		MenuGroup anotherMenuGroup = MenuGroup.from("한마리메뉴");

		// when
		when(menuGroupRepository.findAll()).thenReturn(Arrays.asList(menuGroup, anotherMenuGroup));

		// then
		assertThat(menuGroupService.list()).containsExactly(menuGroup, anotherMenuGroup);
	}
}
