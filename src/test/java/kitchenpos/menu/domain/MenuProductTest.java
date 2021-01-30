package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {
	@DisplayName("상품 정보가 없으면 IllegalArgumentException 발생")
	@Test
	void createWhenNoProduct() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> MenuProduct.builder().quantity(2).build());
	}

}
