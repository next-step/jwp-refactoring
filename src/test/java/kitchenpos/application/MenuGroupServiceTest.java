package kitchenpos.application;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Name;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;

@DisplayName("메뉴그룹 요구사항 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

	@Mock
	private MenuGroupRepository menuGroupRepository;

	@InjectMocks
	private MenuGroupService menuGroupService;


	@DisplayName("메뉴 그룹을 등록할 수 있다.")
	@Test
	void createTest() {
		// given
		MenuGroup menuGroup = createMockMenuGroup();
		when(menuGroupRepository.save(any(MenuGroup.class))).thenReturn(menuGroup);

		// when
		MenuGroupResponse response = menuGroupService.create(new MenuGroupRequest("치킨"));

		// than
		assertThat(response.getId()).isEqualTo(1L);
	}

	@DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
	@Test
	void listTest() {
		// given
		MenuGroup menuGroup = createMockMenuGroup();
		when(menuGroupRepository.findAll()).thenReturn(asList(menuGroup));

		// when
		List<MenuGroupResponse> menuGroups = menuGroupService.findMenuGroups();

		// then
		assertThat(menuGroups).isNotEmpty();
		assertThat(menuGroups.get(0).getId()).isEqualTo(1L);
	}

	private MenuGroup createMockMenuGroup() {
		MenuGroup menuGroup = mock(MenuGroup.class);
		when(menuGroup.getId()).thenReturn(1L);
		when(menuGroup.getName()).thenReturn(Name.valueOf("MockMenuGroup"));
		return menuGroup;
	}

}
