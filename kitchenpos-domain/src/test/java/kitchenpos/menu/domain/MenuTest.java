package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.product.domain.Price;

class MenuTest {

	@DisplayName("0원 미만의 메뉴는 등록할 수 없음")
	@Test
	void createWithUnderZeroPrice() {
		assertThatIllegalArgumentException()
			.isThrownBy(() ->
				Menu.of("신메뉴", Price.of(-1), null)
			)
			.withMessage("가격은 0원 이상이어야 합니다.");
	}
}
