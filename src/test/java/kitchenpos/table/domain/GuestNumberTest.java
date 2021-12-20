package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.InvalidArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

class GuestNumberTest {

    @DisplayName("손님의 수는 0 이상 이다.")
    @Test
    void validate() {
        assertThatThrownBy(() -> GuestNumber.valueOf(-1))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessageContaining("이상이어야 합니다.");
    }

    @DisplayName("null 인 경우 기본값 (0) 입력")
    @ParameterizedTest
    @NullSource
    void create(Integer input) {
        GuestNumber guestNumber = GuestNumber.valueOf(input);
        assertThat(guestNumber).isEqualTo(GuestNumber.valueOf(0));
    }
}