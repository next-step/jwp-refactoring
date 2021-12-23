package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ExceptionMessage;

class NumberOfGuestsTest {

    @DisplayName("손님 수는 0명 보다 작을 수 없다.")
    @Test
    void validateNumberOfGuests() {
        assertThatThrownBy(() -> new NumberOfGuests(-1))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(ExceptionMessage.WRONG_VALUE.getMessage());
    }
}
