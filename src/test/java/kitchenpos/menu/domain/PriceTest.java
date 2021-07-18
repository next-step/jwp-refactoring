package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PriceTest {
	@DisplayName("가격을 만든다")
	@Test
	void createPriceInHappyCase() {
		Price price = new Price(new BigDecimal(1000));

		assertThat(price).isEqualTo(new Price(new BigDecimal(1000)));
	}

	@DisplayName("가격은 음수일 수 없다.")
	@Test
	void priceWithMinusAmount() {
		assertThatThrownBy(() -> new Price(new BigDecimal(-1000))).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("가격의 대소관계는 일반적인 수학적 정의를 따른다.")
	@Test
	void priceComparing() {
		assertThat(new Price(new BigDecimal(1000)).isBiggerThan(new BigDecimal(800))).isTrue();
	}
}
