package kitchenpos.application;

import static kitchenpos.TestFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupDao;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;

@DisplayName("메뉴 그룹 BO 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

	@Mock
	private MenuGroupDao menuGroupDao;

	@InjectMocks
	private MenuGroupService menuGroupService;

	@DisplayName("메뉴 그룹 생성 : 이름을 받아 메뉴 그룹을 생성할 수 있음")
	@Test
	void create() {
		MenuGroup 새_메뉴_그룹1 = new MenuGroup.Builder().name("새_메뉴_그룹1").build();
		given(menuGroupDao.save(any(MenuGroup.class))).willAnswer(invocation -> {
			MenuGroup savedMenuGroup = invocation.getArgument(0, MenuGroup.class);
			savedMenuGroup.setId(1L);
			return savedMenuGroup;
		});
		MenuGroupRequest 새_메뉴_그룹1_요청 = new MenuGroupRequest("새_메뉴_그룹1");

		// when
		MenuGroupResponse response = menuGroupService.create(새_메뉴_그룹1_요청);

		// then
		assertAll(
			() -> assertThat(response.getId()).isEqualTo(1L),
			() -> assertThat(response.getName()).isEqualTo(새_메뉴_그룹1.getName())
		);
	}

	@DisplayName("메뉴 그룹 목록 조회 : 조회 결과에는 메뉴 그룹 번호, 메뉴 그룹 이름이 포함됨.")
	@Test
	void list() {
		// given
		given(menuGroupDao.findAll()).willReturn(Arrays.asList(메뉴_그룹1, 메뉴_그룹2));

		// when
		List<MenuGroupResponse> menuGroupList = menuGroupService.list();

		// then
		assertThat(menuGroupList).map(MenuGroupResponse::getId).contains(메뉴_그룹1.getId(), 메뉴_그룹2.getId());
		assertThat(menuGroupList).map(MenuGroupResponse::getName).contains("두마리메뉴", "한마리메뉴");
	}
}
