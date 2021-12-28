package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;

@DisplayName("손님 인원 도메인 테스트")
public class NumberOfGuestsTest {

	@DisplayName("생성 테스트")
	@Test
	void createTest() {
		assertThat(NumberOfGuests.of(123))
			.isEqualTo(NumberOfGuests.of(123));
	}

	@DisplayName("최소값 0 이상이어야 한다")
	@Test
	void validateTest() {
		assertThatThrownBy(() -> NumberOfGuests.of(-1))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

	@DisplayName("null 이면 안된다")
	@Test
	void validateTest2() {
		assertThatThrownBy(() -> NumberOfGuests.of(null))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

}
