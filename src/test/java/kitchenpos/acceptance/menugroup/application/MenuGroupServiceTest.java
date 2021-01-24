package kitchenpos.acceptance.menugroup.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;

@DisplayName("메뉴 그럽 Stubbing 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

	@Mock
	private MenuGroupRepository menuGroupRepository;
	@InjectMocks
	private MenuGroupService menuGroupService;

	@DisplayName("메뉴 그룹: 메뉴 그룹 생성 테스트")
	@Test
	void createTest() {
		// given
		MenuGroup menuGroup = MenuGroup.of("두마리메뉴");
		given(menuGroupRepository.save(any())).willReturn(menuGroup);

		// when
		MenuGroupResponse actual = menuGroupService.create(MenuGroupRequest.of(menuGroup.getName()));

		// then
		assertThat(actual).isNotNull();
	}

	@DisplayName("메뉴 그룹: 메뉴 그룹 조회 테스트")
	@Test
	void listTest() {
		// given
		given(menuGroupRepository.findAll()).willReturn(Arrays.asList(
			MenuGroup.of("두마리메뉴"),
			MenuGroup.of("세마리메뉴")
		));

		// when
		List<MenuGroupResponse> actual = menuGroupService.list();

		// then
		assertThat(actual).hasSize(2);
	}
}
