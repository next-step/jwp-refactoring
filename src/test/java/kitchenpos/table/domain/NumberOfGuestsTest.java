package kitchenpos.table.domain;

import kitchenpos.table.domain.exception.InvalidNumberOfGuestsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NumberOfGuestsTest {

    @Test
    void create() {
        //when
        NumberOfGuests numberOfGuests = NumberOfGuests.of(10);

        //then
        assertThat(numberOfGuests.getValue()).isEqualTo(10L);
    }

    @DisplayName("테이블 인원은 0보다 작을 수 없습니다.")
    @Test
    void createException() {
        //when
        assertThatThrownBy(() -> NumberOfGuests.of(-1))
                .isInstanceOf(InvalidNumberOfGuestsException.class); //then
    }
}
