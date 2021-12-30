package kitchenpos.ordertable.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.ordertable.exception.IllegalNumberOfGuestsException;

import kitchenpos.ordertable.vo.NumberOfGuests;
import org.junit.jupiter.api.Test;

class NumberOfGuestsTest {

    @Test
    void constructor() {
        int guests = 5;
        NumberOfGuests numberOfGuests = new NumberOfGuests(guests);
        assertThat(numberOfGuests).isEqualTo(new NumberOfGuests(guests));
    }

    @Test
    void constructor_exception() {
        assertThatThrownBy(() -> new NumberOfGuests(-1))
            .isInstanceOf(IllegalNumberOfGuestsException.class);
    }

}
