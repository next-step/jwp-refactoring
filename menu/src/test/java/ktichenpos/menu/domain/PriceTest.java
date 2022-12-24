package ktichenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ktichenpos.menu.menu.domain.Price;

@DisplayName("가격 테스트")
class PriceTest {

	@Test
	@DisplayName("가격 생성")
	void createPriceTest() {
		assertThatNoException()
			.isThrownBy(() -> Price.from(BigDecimal.valueOf(1000L)));
	}

	@Test
	@DisplayName("가격 생성 - 값이 null 이면 예외 발생")
	void createPriceWithNullValueTest() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Price.from(null))
			.withMessage("가격은 필수입니다.");
	}

	@ParameterizedTest(name = "[{index}] {argumentsWithNames} 으로 생성 불가능")
	@DisplayName("가격 생성 - 값이 0 이하이면 예외 발생")
	@ValueSource(longs = {-1L, -1000L})
	void createPriceWithInvalidValueTest(long value) {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Price.from(BigDecimal.valueOf(value)))
			.withMessageContaining("0 이상이어야 합니다.");
	}

	@Test
	@DisplayName("가격 합산")
	void sumPriceTest() {
		// given
		Price price1 = Price.from(BigDecimal.valueOf(1000L));
		Price price2 = Price.from(BigDecimal.valueOf(2000L));

		// when
		Price sumPrice = price1.sum(price2);

		// then
		assertThat(sumPrice).isEqualTo(Price.from(BigDecimal.valueOf(3000L)));
	}

	@Test
	@DisplayName("가격 곱셈")
	void multiplyPriceTest() {
		// given
		Price price = Price.from(BigDecimal.valueOf(1000L));
		long count = 3L;

		// when
		Price multiplyPrice = price.multiply(count);

		// then
		assertThat(multiplyPrice).isEqualTo(Price.from(BigDecimal.valueOf(3000L)));
	}
}
