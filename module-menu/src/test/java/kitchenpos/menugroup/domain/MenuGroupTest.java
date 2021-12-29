package kitchenpos.menugroup.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupTest {

	@Test
	@DisplayName("메뉴 그룹 생성 테스트")
	public void createMenuGroupTest() {
		//when
		MenuGroup menuGroup = new MenuGroup(1L, "후라이드+양념");

		//then
		assertThat(menuGroup).isNotNull();
		assertThat(menuGroup.getId()).isEqualTo(1L);
		assertThat(menuGroup.getName()).isEqualTo("후라이드+양념");
	}
}
