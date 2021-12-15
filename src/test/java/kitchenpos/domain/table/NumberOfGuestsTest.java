package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NumberOfGuestsTest {

    @DisplayName("NumberOfGuests 는 0 이상의 숫자로 생성할 수 있다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 10, 100})
    void create1(int numberOfGuests) {
        // when & then
        assertThatNoException().isThrownBy(() -> NumberOfGuests.from(numberOfGuests));
    }

    @DisplayName("NumberOfGuests 는 0 미만의 음수로 생성 시, 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -10, -100})
    void create2(int numberOfGuests) {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> NumberOfGuests.from(numberOfGuests))
                                            .withMessageContaining("NumberOfGuests 는 0 이상의 숫자로 생성할 수 있습니다.");
    }
}