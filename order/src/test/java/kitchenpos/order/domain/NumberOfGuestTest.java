package kitchenpos.order.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NumberOfGuestTest {

    @Test
    @DisplayName("손님의 수 생성")
    void createNumberOfGuest() {
        //when & then
        NumberOfGuest numberOfGuest = new NumberOfGuest(1);
    }

    @Test
    @DisplayName("손님의 수는 음수일 수 없다")
    void validateNumberOfGuest() {
        //when & then
        Assertions.assertThatThrownBy(() -> new NumberOfGuest(-1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("손님의 수는 0명 이하일 수 없습니다.");
    }
}