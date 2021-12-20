package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("상품 가격")
class ProductPriceTest {

	@DisplayName("생성")
	@Test
	void of() {
		// given
		BigDecimal price = BigDecimal.valueOf(17000L);

		// when
		ProductPrice productPrice = ProductPrice.of(price);

		// then
		assertThat(productPrice.getValue()).isEqualTo(price);
	}

	@DisplayName("생성 실패 - 가격이 없거나 음수인 경우")
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"-10000"})
	void ofFailOnNullOrNegativePrice(BigDecimal price) {
		// given

		// when
		ThrowingCallable throwingCallable = () -> ProductPrice.of(price);

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("동등성 비교")
	@Test
	void equals() {
		// given
		BigDecimal price = BigDecimal.valueOf(17000L);

		// when
		ProductPrice actual = ProductPrice.of(price);
		ProductPrice expected = ProductPrice.of(price);

		// then
		assertThat(actual).isEqualTo(expected);
	}
}
