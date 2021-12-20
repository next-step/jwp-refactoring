package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("수량")
class MenuProductQuantityTest {

	@DisplayName("생성")
	@Test
	void of() {
		// given
		long quantity = 2;

		// when
		MenuProductQuantity menuProductQuantity = MenuProductQuantity.of(quantity);

		// then
		assertThat(menuProductQuantity.getValue()).isEqualTo(quantity);
	}

	@DisplayName("생성 실패 - 수량이 없거나 음수인 경우")
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"-1"})
	void ofFailOnNullOrNegativeQuantity(Long quantity) {
		// given

		// when
		ThrowingCallable throwingCallable = () -> MenuProductQuantity.of(quantity);

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("동등성 비교")
	@Test
	void equals() {
		// given
		long quantity = 2;

		// when
		MenuProductQuantity actual = MenuProductQuantity.of(quantity);
		MenuProductQuantity expected = MenuProductQuantity.of(quantity);

		// then
		assertThat(actual).isEqualTo(expected);
	}
}
