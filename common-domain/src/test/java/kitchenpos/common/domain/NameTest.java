package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ExceptionMessage;

class NameTest {

    @Test
    @DisplayName("이름은 필수값이다.")
    void validateName() {
        assertThatThrownBy(() -> new Name(""))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(ExceptionMessage.WRONG_VALUE.getMessage());
    }

}
