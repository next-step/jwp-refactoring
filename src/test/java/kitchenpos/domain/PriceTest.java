package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.ErrorCode;
import kitchenpos.common.PriceException;

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
		}).isInstanceOf(PriceException.class)
			.hasMessageContaining(ErrorCode.PRICE_NOT_NEGATIVE_NUMBER.getMessage());
	}

	@DisplayName("가격이 존재하지 않을 경우 예외처리 테스트")
	@Test
	void validateIsNullPrice() {
		// given // when // then
		assertThatThrownBy(() -> {
			Price.from((Integer)null);
		}).isInstanceOf(PriceException.class)
			.hasMessageContaining(ErrorCode.PRICE_IS_NOT_NULL.getMessage());
	}

	@DisplayName("가격을 더하는 테스트")
	@Test
	void addPrice() {
		// given
		Price p1 = Price.from(1000);
		Price p2 = Price.from(1000);
		// when
		Price sumPrice = p1.plus(p2);

		// then
		assertThat(sumPrice.getPrice().intValue()).isEqualTo(2000);
	}

	@DisplayName("가격을 비교하는 테스트")
	@Test
	void comparePrice() {
		// given
		Price p1 = Price.from(1001);
		Price p2 = Price.from(1000);

		// when
		boolean result = p1.compare(p2);

		// then
		assertThat(result).isTrue();
	}

}
