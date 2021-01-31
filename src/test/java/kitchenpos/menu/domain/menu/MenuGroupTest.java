package kitchenpos.menu.domain.menu;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 그룹 도메인 테스트")
public class MenuGroupTest {

	@DisplayName("메뉴 그룹 생성")
	@Test
	void create() {
		final String name = "후라이드치킨";
		MenuGroup 후라이드치킨 = new MenuGroup(name);

		assertThat(후라이드치킨).isNotNull();
		assertThat(후라이드치킨.getName()).isEqualTo(name);
	}
}
