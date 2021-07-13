package kitchenpos.menu;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.MenuGroup;

@DisplayName("메뉴 그룹 도메인 테스트")
public class MenuGroupTest {

	@DisplayName("메뉴 그룹 생성 테스트")
	@Test
	void create() {
		MenuGroup menuGroup = new MenuGroup(1L, "메뉴그룹");
		assertThat(menuGroup).isNotNull();
		assertThat(menuGroup.getId()).isEqualTo(1L);
		assertThat(menuGroup.getName()).isEqualTo("메뉴그룹");
	}
}
