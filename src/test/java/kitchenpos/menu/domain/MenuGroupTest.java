package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 그룹 도메인 테스트")
public class MenuGroupTest {

	public static final MenuGroup 추천메뉴 = MenuGroup.of(1L, "추천메뉴");

	@DisplayName("생성 테스트")
	@Test
	void createTest() {
		assertThat(MenuGroup.of(1L, "추천메뉴"))
			.isEqualTo(MenuGroup.of(1L, "추천메뉴"));
	}
}
