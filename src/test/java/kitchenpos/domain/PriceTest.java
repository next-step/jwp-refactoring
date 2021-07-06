package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {

	@DisplayName("가격은 0보다 작을 수 없다.")
	@Test
	void testCreatePriceError() {
		assertThatThrownBy(() -> {
			new Price(BigDecimal.valueOf(-1));
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("상품 가격은 0보다 작을 수 없습니다.");
	}

	@DisplayName("가격은 null이 될 수 없다.")
	@Test
	void testCreatePriceErrorNull() {
		assertThatThrownBy(() -> {
			new Price(null);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("상품 가격은 0보다 작을 수 없습니다.");
	}

	@DisplayName("0이상 금액에 대해 가격 정상 생성")
	@Test
	void testCreatePrice() {
		Price actual = new Price(BigDecimal.valueOf(20000));
		assertThat(actual).isEqualTo(new Price(BigDecimal.valueOf(20000)));
	}

}