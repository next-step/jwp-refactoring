package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.exception.InvalidMoneyValueException;

class MoneyTest {

	@DisplayName("금액은 0보다 작을 수 없다")
	@ParameterizedTest
	@ValueSource(ints ={-1, -100, Integer.MIN_VALUE})
	void testValidate(int price) {
		assertThatThrownBy(() ->Money.wons(price))
			.isInstanceOf(InvalidMoneyValueException.class);
	}

	@DisplayName("금액은 null일 수 없다")
	@Test
	void testValidateNotNull() {
		assertThatThrownBy(() ->Money.wons(null))
			.isInstanceOf(InvalidMoneyValueException.class);
	}

	@DisplayName("금액 생성 테스트")
	@ParameterizedTest
	@ValueSource(ints = {0, 1, Integer.MAX_VALUE})
	void testValueOf(int price) {
		assertThat(Money.wons(price).isEqualTo(price))
			.isTrue();
	}

}
