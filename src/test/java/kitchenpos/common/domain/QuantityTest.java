package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ExceptionMessage;

class QuantityTest {

    @DisplayName("수량은 0보다 크거나 같아야 한다.")
    @Test
    void validateQuantity() {
        assertThatThrownBy(() -> new Quantity(-1))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(ExceptionMessage.WRONG_VALUE.getMessage());
    }
}
