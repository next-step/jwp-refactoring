package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

	@InjectMocks
	private MenuGroupService menuGroupService;

	@Mock
	private MenuGroupRepository menuGroupRepository;

	@DisplayName("메뉴 그룹을 생성한다")
	@Test
	void createTest() {
		// given
		MenuGroupRequest request = new MenuGroupRequest("추천메뉴");

		MenuGroup persist = MenuGroup.of(1L, "추천메뉴");

		given(menuGroupRepository.save(any())).willReturn(persist);

		// when
		MenuGroupResponse result = menuGroupService.create(request);

		// then
		assertThat(result.getId()).isEqualTo(persist.getId());
		assertThat(result.getName()).isEqualTo(persist.getName().toText());
	}

	@DisplayName("메뉴 그룹 목록을 조회한다")
	@Test
	void listTest() {
		// given
		MenuGroup menuGroup1 = MenuGroup.of(1L, "추천메뉴");
		List<MenuGroup> persist = new ArrayList<>();
		persist.add(menuGroup1);

		given(menuGroupRepository.findAll()).willReturn(persist);

		// when
		List<MenuGroupResponse> result = menuGroupService.getList();

		// then
		assertThat(result.size()).isEqualTo(persist.size());
	}
}
