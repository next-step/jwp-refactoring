package kitchenpos.table.domain;

import kitchenpos.table.exception.NegativeNumberOfGuestsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("손님 숫자 도메인 테스트")
class NumberOfGuestsTest {
    @DisplayName("생성 테스트")
    @Test
    void createTest() {
        assertThat(NumberOfGuests.from(1))
                .isEqualTo(NumberOfGuests.from(1));
    }

    @DisplayName("최소값 0 이상이어야 한다")
    @Test
    void validateTest() {
        assertThatThrownBy(() -> NumberOfGuests.from(-1))
                .isInstanceOf(NegativeNumberOfGuestsException.class);
    }
}
