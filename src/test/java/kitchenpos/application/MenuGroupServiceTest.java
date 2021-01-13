package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.MySpringBootTest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySpringBootTest
class MenuGroupServiceTest {

	@Autowired
	private MenuGroupService menuGroupService;

	@DisplayName("메뉴그룹을 추가한다.")
	@Test
	void create() {
		MenuGroup menuGroup = new MenuGroup("샐러드");

		//when
		MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

		//then
		List<MenuGroup> list = menuGroupService.list();
		assertThat(list).contains(savedMenuGroup);
	}
}
