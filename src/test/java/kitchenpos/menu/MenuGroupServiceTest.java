package kitchenpos.menu;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;

@DisplayName("메뉴 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

	@Mock
	private MenuGroupRepository menuGroupRepository;

	@InjectMocks
	private MenuGroupService menuGroupService;

	MenuGroupRequest 피자_요청;
	MenuGroupRequest 치킨_요청;
	MenuGroupResponse 피자_응답;
	MenuGroupResponse 치킨_응답;

	@BeforeEach
	void setUp() {
		피자_요청 = 메뉴그룹_생성_요청("피자");
		치킨_요청 = 메뉴그룹_생성_요청("치킨");
		피자_응답 = new MenuGroupResponse(1L, "피자");
		치킨_응답 = new MenuGroupResponse(2L, "치킨");
	}

	@DisplayName("메뉴 그룹을 생성한다.")
	@Test
	void 메뉴_그룹을_등록한다() {
		given(menuGroupRepository.save(any())).willReturn(피자_요청.toMenuGroup());

		MenuGroupResponse createdMenuGroup = menuGroupService.create(피자_요청);

		assertThat(createdMenuGroup).isNotNull();
		assertThat(createdMenuGroup.getName()).isEqualTo(피자_요청.getName());
	}

	@DisplayName("메뉴 그룹 리스트를 조회한다.")
	@Test
	void 메뉴_그룹_리스트를_조회한다() {
		given(menuGroupRepository.findAll()).willReturn(Arrays.asList(피자_요청.toMenuGroup(), 치킨_요청.toMenuGroup()));

		List<MenuGroupResponse> 메뉴그룹들 = menuGroupService.list();

		assertThat(메뉴그룹들).hasSize(2);
	}

	public static MenuGroupRequest 메뉴그룹_생성_요청(String name) {
		MenuGroupRequest 메뉴그룹_요청 = new MenuGroupRequest(name);
		return 메뉴그룹_요청;
	}

}
