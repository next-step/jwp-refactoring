package kitchenpos.table.domain;

import kitchenpos.table.exception.InvalidNumberOfGuestsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("NumberOfGuests 클래스 테스트")
class NumberOfGuestsTest {

    @DisplayName("1인 NumberOfGuests를 생성한다.")
    @Test
    void successfulCreate() {
        NumberOfGuests quantity = new NumberOfGuests(1);
        assertThat(quantity.getValue()).isEqualTo(1);
    }

    @DisplayName("-1인 NumberOfGuests를 생성한다.")
    @Test
    void failureCreate() {
        assertThatThrownBy(() -> {
            new NumberOfGuests(-1);
        }).isInstanceOf(InvalidNumberOfGuestsException.class)
          .hasMessageContaining("유효하지 않은 손님 수입니다.");
    }

    @DisplayName("NumberOfGuests(10)과 NumberOfGuests(10)은 동등하다.")
    @Test
    void equals() {
        NumberOfGuests ten = new NumberOfGuests(10);
        assertThat(ten).isEqualTo(new NumberOfGuests(10));
    }
}
