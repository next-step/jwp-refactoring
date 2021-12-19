package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ExceptionMessage;

class PriceTest {

    @DisplayName("가격은 필수값이다.")
    @Test
    void validatePrice() {
        assertThatThrownBy(() -> new Price(null))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(ExceptionMessage.WRONG_VALUE.getMessage());
    }

}
