package kitchenpos.table.domain;

import kitchenpos.exception.IllegalNumberOfGuestsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("손님 수 원시값 포장 객체 테스트")
class NumberOfGuestsTest {


    @Test
    void 손님_수_원시값_포장_객체_생성() {
        NumberOfGuests numberOfGuests = new NumberOfGuests(1);
        assertThat(numberOfGuests).isEqualTo(new NumberOfGuests(1));
    }

    @Test
    void 손님_수를_음수_입력_시_에러_발생() {
        assertThatThrownBy(() -> new NumberOfGuests(-1)).isInstanceOf(IllegalNumberOfGuestsException.class);
    }
}
