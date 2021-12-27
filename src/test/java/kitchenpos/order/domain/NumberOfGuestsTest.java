package kitchenpos.order.domain;

import kitchenpos.common.exception.InputDataErrorCode;
import kitchenpos.common.exception.InputDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("NumberOfGuest 단위 테스트")
class NumberOfGuestsTest {

    @Test
    @DisplayName("손님 숫자를 저장한다.")
    void saveNumberOfGuestsTest(){
        NumberOfGuests numberOfGuests = new NumberOfGuests(5);
        assertThat(numberOfGuests.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    @DisplayName("손님의 숫자가 음수로 저장하면 에러 처리 테스트")
    void saveWrongNumberOfGuestsTest(){
        assertThatThrownBy(() -> {
            new NumberOfGuests(-5);
        }).isInstanceOf(InputDataException.class)
                .hasMessageContaining(InputDataErrorCode.THE_NUMBER_OF_GUESTS_IS_LESS_THAN_ZERO.errorMessage());
    }
}
