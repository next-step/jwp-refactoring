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

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

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
		MenuGroup 새_메뉴_그룹1 = newMenuGroup(null, "새_메뉴_그룹1");
		given(menuGroupDao.save(새_메뉴_그룹1)).willAnswer(invocation -> {
			새_메뉴_그룹1.setId(1L);
			return 새_메뉴_그룹1;
		});

		// when
		MenuGroup menuGroup = menuGroupService.create(새_메뉴_그룹1);

		// then
		assertAll(
			() -> assertThat(menuGroup.getId()).isEqualTo(1L),
			() -> assertThat(menuGroup.getName()).isEqualTo(새_메뉴_그룹1.getName())
		);
	}

	@DisplayName("메뉴 그룹 목록 조회 : 조회 결과에는 메뉴 그룹 번호, 메뉴 그룹 이름이 포함됨.")
	@Test
	void list() {
		// given
		given(menuGroupDao.findAll()).willReturn(Arrays.asList(메뉴_그룹1, 메뉴_그룹2));

		// when
		List<MenuGroup> menuGroupList = menuGroupService.list();

		// then
		assertThat(menuGroupList).map(MenuGroup::getId).contains(메뉴_그룹1.getId(), 메뉴_그룹2.getId());
		assertThat(menuGroupList).map(MenuGroup::getName).contains("두마리메뉴", "한마리메뉴");
	}
}
