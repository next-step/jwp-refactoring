package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

	public static Product 후라이드치킨 = new Product("후라이드치킨", Price.wonOf(1000));
	public static Product 피자 = new Product("피자", Price.wonOf(2000));

	@DisplayName("가격이 음수인 상품은 생성 될 수 없다.")
	@Test
	void negativePriceTest() {
		assertThatThrownBy(() -> new Product("상품", Price.wonOf(BigDecimal.valueOf(-1))))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("가격은 음수가 될 수 없습니다.");
	}
}