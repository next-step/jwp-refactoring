package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;

@DisplayName("양의 정수 도메인 테스트")
public class PositiveNumberTest {

	@DisplayName("생성 테스트")
	@Test
	void createTest() {
		assertThat(PositiveNumber.valueOf(123L))
			.isEqualTo(PositiveNumber.valueOf(123L));
	}

	@DisplayName("최소값 0 이상이어야 한다")
	@Test
	void validateTest() {
		assertThatThrownBy(() -> PositiveNumber.valueOf(-1L))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

	@DisplayName("null 이면 안된다")
	@Test
	void validateTest2() {
		assertThatThrownBy(() -> PositiveNumber.valueOf(null))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

}
