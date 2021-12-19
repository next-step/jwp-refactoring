package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ExceptionMessage;

class PriceTest {

    @DisplayName("가격은 null 이거나 0보다 작을 수 없다.")
    @Test
    void validatePrice() {
        assertAll(
            () -> assertThatThrownBy(() -> new Price(null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.WRONG_VALUE.getMessage()),
            () -> assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-1)))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.WRONG_VALUE.getMessage())
        );
    }

}
