package kitchenpos.common.unit;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.common.Price;
import kitchenpos.exception.ErrorMessage;

public class PriceTest {
	@DisplayName("객체 생성시 가격이 없으면 에러가 발생한다.")
	@Test
	void create_price_null_exception() {
		// when - then
		assertThatThrownBy(() -> Price.of(null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorMessage.cannotBeNull("가격"));
	}

	@ParameterizedTest(name = "객체 생성시 가격이 음수면 에러가 발생한다. [가격: {0}]")
	@ValueSource(ints = {-1, -2})
	void create_price_negative_exception(int price) {
		// when - then
		assertThatThrownBy(() -> Price.of(price))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorMessage.cannotBeNegative("가격"));
	}
}
