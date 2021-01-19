package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

	@DisplayName("0원 미만의 메뉴는 등록할 수 없음")
	@Test
	void createWithUnderZeroPrice() {
		assertThatIllegalArgumentException()
			  .isThrownBy(() ->
					new Menu("신메뉴", new BigDecimal(-1), null)
			  )
			  .withMessage("메뉴의 가격은 0원 이상이어야 합니다.");
	}
}
