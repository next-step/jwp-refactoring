package kitchenpos.menugroup.application;

import static org.assertj.core.api.Assertions.*;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;

@Transactional
@SpringBootTest
class MenuGroupServiceTest {

	@Autowired
	private MenuGroupService menuGroupService;

	@Test
	@DisplayName("메뉴 그룹 생성 테스트")
	public void createMenuGroupTest() {
		//given
		MenuGroupRequest menuGroupRequest = new MenuGroupRequest("후라이드+양념");
		//when
		MenuGroup createMenuGroup = menuGroupService.create(menuGroupRequest);
		//then
		assertThat(createMenuGroup).isNotNull();
		assertThat(createMenuGroup).isEqualTo(new MenuGroup(5L, "후라이드+양념"));
	}

	@Test
	@DisplayName("메뉴 그룹 목록 조회 테스트")
	public void findMenuGroupList() {
		//given
		//when
		List<MenuGroup> menuGroups = menuGroupService.list();
		//then
		assertThat(menuGroups).hasSize(4);
	}
}
