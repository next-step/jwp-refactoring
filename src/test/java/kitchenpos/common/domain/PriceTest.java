package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;

@DisplayName("가격 도메인 테스트")
public class PriceTest {

	@DisplayName("생성 테스트")
	@Test
	void create() {
		assertThat(Price.of(BigDecimal.ONE)).isEqualTo(Price.of(BigDecimal.ONE));
	}

	@DisplayName("가격이 최소값(0) 이상이어야 한다")
	@Test
	void validateTest1() {
		// given
		BigDecimal number = BigDecimal.valueOf(-1);

		// when, then
		assertThatThrownBy(() -> Price.of(number))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

	@DisplayName("가격이 null 이면 안된다")
	@Test
	void validateTest2() {
		assertThatThrownBy(() -> Price.of(null))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

}
