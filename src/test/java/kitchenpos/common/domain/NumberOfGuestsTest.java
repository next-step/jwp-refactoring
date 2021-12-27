package kitchenpos.common.domain;

import kitchenpos.common.exception.NegativeNumberOfGuestsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("손님 숫자 도메인 테스트")
class NumberOfGuestsTest {

    @DisplayName("손님 숫자는 0 미만이 될 수 없다.")
    @Test
    void createNumberOfGuestsNegativeNumberExceptionTest() {
        assertThatThrownBy(() -> {
            // when
            NumberOfGuests numberOfGuests = new NumberOfGuests(-1);

            // then
        }).isInstanceOf(NegativeNumberOfGuestsException.class);
    }

}