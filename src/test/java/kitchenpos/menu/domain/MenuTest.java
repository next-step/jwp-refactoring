package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 도메인 테스트")
public class MenuTest {

	public static final Menu 후라이드둘 = Menu.of(1L, "후라이드둘", BigDecimal.valueOf(30_000), MenuGroupTest.추천메뉴);

	@DisplayName("생성 테스트")
	@Test
	void createTest() {
		assertThat(Menu.of(1L, "후라이드", BigDecimal.ONE, MenuGroup.of(1L, "추천메뉴")))
			.isEqualTo(Menu.of(1L, "후라이드", BigDecimal.ONE, MenuGroup.of(1L, "추천메뉴")));
	}
}
