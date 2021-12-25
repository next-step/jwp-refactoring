package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.ordertable.exception.IllegalNumberOfGuests;
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
            .isInstanceOf(IllegalNumberOfGuests.class);
    }

}
