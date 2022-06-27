package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("방문 손님 수 관련 Domain 단위 테스트")
class NumberOfGuestTest {

    @DisplayName("방문 손님 수는 0명 미만일 수 없다.")
    @Test
    void updateNumberOfGuests_less_then_one() {
        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new NumberOfGuest(-1));
    }
}
