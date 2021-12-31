package kitchenpos.table.domain;

import kitchenpos.exception.GuestsNumberNegativeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NumberOfGuestsTest {

    @Test
    @DisplayName("테이블 인원 생성")
    public void create() {
        // given
        // when
        NumberOfGuests actual = new NumberOfGuests(0);
        // then
        assertThat(actual).isEqualTo(new NumberOfGuests(0));
    }

    @Test
    @DisplayName("테이블 인원은 0명보다 작을수 없습니다.")
    public void guestsNumberNegativeExceptionTest() {
        assertThatThrownBy(() -> new NumberOfGuests(-1)).isInstanceOf(GuestsNumberNegativeException.class);
    }
}