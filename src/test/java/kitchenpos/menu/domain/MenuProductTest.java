package kitchenpos.menu.domain;

import static kitchenpos.domain.TestFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuProductTest {

	@Test
	@DisplayName("메뉴 구성 상품의 가격과 메뉴에 포함된 수량을 곱셈해야 한다.")
	void priceSum() {
		//given
		BigDecimal price = 메뉴상품_후라이드.getProductPrice();
		long quantity = 메뉴상품_후라이드.getQuantity();
		BigDecimal expected = price.multiply(BigDecimal.valueOf(quantity));

		//when
		BigDecimal pricePerQuantity = 메뉴상품_후라이드.priceForQuantity();

		//then
		assertThat(pricePerQuantity).isEqualByComparingTo(expected);
	}
}
