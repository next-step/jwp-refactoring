package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

public class NumberOfGuestsTest {

    @DisplayName("손님 수는 0보다 작은 숫자로 생성할 수 없다")
    @Test
    void createNumberOfGuestsByMinusTest() {
        assertThatIllegalArgumentException().isThrownBy(() -> 손님_수_생성(-1));
    }

    @DisplayName("0보다 큰 숫자로 손님 수를 생성하면 정상 생성되어야 한다")
    @Test
    void createNumberOfGuestsTest() {
        assertThatNoException().isThrownBy(() -> 손님_수_생성(0));
        assertThatNoException().isThrownBy(() -> 손님_수_생성(Integer.MAX_VALUE));
    }

    public static NumberOfGuests 손님_수_생성(int value) {
        return new NumberOfGuests(value);
    }
}