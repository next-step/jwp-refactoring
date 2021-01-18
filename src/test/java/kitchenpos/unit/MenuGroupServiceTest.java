package kitchenpos.unit;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MenuGroupServiceTest {

	@Autowired
	private MenuGroupService menuGroupService;

	@Test
	@DisplayName("메뉴 그룹을등록한다")
	void createMenuGroup() {
		MenuGroupRequest menuGroupRequest = new MenuGroupRequest("겨울신메뉴");
		MenuGroupResponse menuGroupResponse = menuGroupService.create(menuGroupRequest);
		assertThat(menuGroupResponse.getName()).isEqualTo(menuGroupRequest.getName());
	}


	@Test
	@DisplayName("메뉴 그룹 목록을조회한다")
	void listMenuGroups() {
		this.createMenuGroup();
		List<MenuGroupResponse> menuGroupResponses = menuGroupService.listMenuGroups();
		assertThat(menuGroupResponses.size()).isEqualTo(6);

	}
}
