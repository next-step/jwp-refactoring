package kitchenpos.ordertable.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("방문한 손님 수 도메인 테스트")
class NumberOfGuestsTest {

    @Test
    @DisplayName("방문한 손님 수를 생성한다.")
    void create() {
        // when
        NumberOfGuests numberOfGuests = new NumberOfGuests(1);

        // then
        assertThat(numberOfGuests).isEqualTo(new NumberOfGuests(1));
    }

    @Test
    @DisplayName("0보다 작은 숫자로 방문한 손님 수를 생성하면 예외를 발생한다.")
    void createThrowException() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new NumberOfGuests(-1))
                .withMessageMatching(NumberOfGuests.MESSAGE_VALIDATE);
    }
}
