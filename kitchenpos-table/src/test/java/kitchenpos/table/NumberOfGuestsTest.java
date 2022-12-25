package kitchenpos.table;

import kitchenpos.table.domain.NumberOfGuests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NumberOfGuestsTest {

    @DisplayName("0보다 작은 숫자의 게스트 생성 테스트")
    @Test
    void createNumberOfGuestsUnderZero() {
        assertThatThrownBy(() -> new NumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("게스트 숫자 0 생성 테스트")
    @Test
    void createNumberOfGuestsZero() {
        assertThat(new NumberOfGuests(0))
                .isNotNull();
    }

    @DisplayName("동일성 테스트")
    @Test
    void equalsTest() {
        //given
        final NumberOfGuests guestNumber1 = new NumberOfGuests(10);
        final NumberOfGuests guestNumber2 = new NumberOfGuests(10);

        //when
        //then
        assertThat(guestNumber1)
                .isEqualTo(guestNumber2);
    }
}
