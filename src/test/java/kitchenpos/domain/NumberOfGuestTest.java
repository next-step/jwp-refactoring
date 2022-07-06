package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NumberOfGuestTest {

    @Test
    @DisplayName("사람의 수는 0명이상 이어야 합니다.")
    void validNumberOfGuest() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> NumberOfGuest.from(-1));

    }

}