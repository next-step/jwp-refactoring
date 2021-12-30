package kitchenpos.moduledomain.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.moduledomain.common.exception.Message;
import org.junit.jupiter.api.Test;

class NumberOfGuestsTest {

    @Test
    void 손님의_수는_0보다_작을_수_없다() {

        assertThatThrownBy(() -> {
            NumberOfGuests numberOfGuests = new NumberOfGuests(-1);
        }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(Message.NUMBER_OF_GUEST_SMALL_THAN_ZERO.getMessage());
    }


    @Test
    void 손님의_수가_0보다크다면_정상생성() {

        // when
        NumberOfGuests numberOfGuests = new NumberOfGuests(0);

        // then
        assertThat(numberOfGuests).isNotNull();
    }
}