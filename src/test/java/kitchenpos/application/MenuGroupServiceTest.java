package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
	private static final String NAME = "후라이드+양념";
	private static final String OTHER_NAME = "로제+양념";

	@Mock
	private MenuGroupDao menuGroupDao;

	@Test
	@DisplayName("메뉴 그룹 생성 테스트")
	public void createMenuGroupTest() {
		//given
		MenuGroup menuGroup = new MenuGroup(null, NAME);
		Mockito.when(menuGroupDao.save(menuGroup)).thenReturn(new MenuGroup(1L, NAME));
		//when
		MenuGroupService menuGroupService = new MenuGroupService(menuGroupDao);
		MenuGroup createMenuGroup = menuGroupService.create(menuGroup);
		//then
		assertThat(createMenuGroup).isNotNull();
		assertThat(createMenuGroup.getId()).isEqualTo(1L);
		assertThat(createMenuGroup.getName()).isEqualTo(NAME);
	}

	@Test
	@DisplayName("메뉴 그룹 목록 조회 테스트")
	public void findMenuGroupList() {
		//given
		MenuGroup menuGroup = new MenuGroup(null, NAME);
		MenuGroup otherMenuGroup = new MenuGroup(null, OTHER_NAME);
		Mockito.when(menuGroupDao.findAll()).thenReturn(Lists.newArrayList(menuGroup, otherMenuGroup));
		//when
		MenuGroupService menuGroupService = new MenuGroupService(menuGroupDao);
		List<MenuGroup> menuGroups = menuGroupService.list();
		//then
		assertThat(menuGroups).hasSize(2);
		assertThat(menuGroups).containsExactly(menuGroup, otherMenuGroup);

	}
}
