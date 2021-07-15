package kitchenpos.menu.application;

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

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.repository.MenuGroupRepository;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

	@Mock
	private MenuGroupRepository menuGroupRepository;

	@InjectMocks
	private MenuGroupService menuGroupService;

	@DisplayName("메뉴 그룹 생성 테스트")
	@Test
	void testCreateMenuGroup() {
		MenuGroupRequest menuGroupRequest = new MenuGroupRequest("중식");

		MenuGroup menuGroup = new MenuGroup(menuGroupRequest.getName());
		MenuGroup expected = new MenuGroup("중식");

		when(menuGroupRepository.save(menuGroup)).thenReturn(expected);
		MenuGroupResponse actual = menuGroupService.create(menuGroupRequest);

		verify(menuGroupRepository, times(1)).save(menuGroup);
		assertThat(actual.getId()).isEqualTo(expected.getId());
		assertThat(actual.getName()).isEqualTo(expected.getName());
	}

	@DisplayName("메뉴 그룹 목록 조회 테스트")
	@Test
	void testList() {
		List<MenuGroup> expected = new ArrayList<>();
		expected.add(new MenuGroup("menuGroup1"));
		expected.add(new MenuGroup("menuGroup2"));
		expected.add(new MenuGroup("menuGroup3"));
		List<Long> savedMenuGroupIds = expected.stream().map(MenuGroup::getId).collect(Collectors.toList());

		when(menuGroupRepository.findAll()).thenReturn(expected);

		List<MenuGroupResponse> actual = menuGroupService.list();

		verify(menuGroupRepository, times(1)).findAll();
		List<MenuGroupResponse> expectedMenuGroupResponses = expected.stream()
			.map(MenuGroupResponse::of)
			.collect(Collectors.toList());
		assertThat(actual)
			.containsExactlyElementsOf(expectedMenuGroupResponses);
	}
}