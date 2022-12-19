package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import kitchenpos.common.exception.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("인원 수 테스트")
class NumberOfGuestsTest {
    @Test
    @DisplayName("인원 수 객체 생성")
    void createNumberOfGuests() {
        // when
        NumberOfGuests actual = NumberOfGuests.from(0);

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isInstanceOf(NumberOfGuests.class)
        );
    }

    @Test
    @DisplayName("인원 수는 음수일 수 없다.")
    void createNumberOfGuestsByNegative() {
        assertThatThrownBy(() -> NumberOfGuests.from(-1))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("최소 인원 수는 0명 이상입니다.");
    }
}
