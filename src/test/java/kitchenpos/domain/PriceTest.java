package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.ErrorCode;
import kitchenpos.product.exception.ProductException;

@DisplayName("단위 테스트 : 가격 도메인")
class PriceTest {

	@DisplayName("가격 생성 테스트")
	@Test
	void createPriceIntValue() {
		// given
		int intValue = 1000;

		// when
		Price price = Price.from(intValue);

		// then
		assertThat(price.getPrice().intValue()).isEqualTo(intValue);
	}

	@DisplayName("음수로 가격을 생성할 경우 예외처리 테스트")
	@Test
	void validateUnderZeroPrice() {
		// given
		int intValue = -1000;

		// when // then
		assertThatThrownBy(() -> {
			Price.from(intValue);
		}).isInstanceOf(ProductException.class)
			.hasMessageContaining(ErrorCode.PRICE_NOT_NEGATIVE_NUMBER.getMessage());
	}

	@DisplayName("가격이 존재하지 않을 경우 예외처리 테스트")
	@Test
	void validateIsNullPrice() {
		// given // when // then
		assertThatThrownBy(() -> {
			Price.from(null);
		}).isInstanceOf(ProductException.class)
			.hasMessageContaining(ErrorCode.PRICE_IS_NOT_NULL.getMessage());
	}
}