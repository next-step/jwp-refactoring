package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("고객 수 테스트")
class NumberOfGuestsTest {

	@Test
	@DisplayName("고객 수 생성")
	void createNumberOfGuestsTest() {
		assertThatNoException()
			.isThrownBy(() -> NumberOfGuests.from(1));
	}

	@DisplayName("고객 수 생성 - 0명 보다 작으면 예외 발생")
	@ParameterizedTest(name = "{index} => numberOfGuests={0}")
	@ValueSource(ints = {-1, -2, -3})
	void createNumberOfGuestsWithNegativeTest(int numberOfGuests) {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> NumberOfGuests.from(numberOfGuests))
			.withMessageContaining("0이상 이어야 합니다.");
	}
}
