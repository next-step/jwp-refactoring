package kitchenpos.common.unit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.common.GuestCount;
import kitchenpos.exception.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("손님수 관련 단위테스트")
public class GuestCountTest {

    @ParameterizedTest(name = "객체 생성시 손님수가 음수면 에러가 발생한다. [손님수가: {0}]")
    @ValueSource(ints = {-1, -2})
    void create_number_of_guests_negative_exception(int number_of_guests) {
        // when - then
        assertThatThrownBy(() -> GuestCount.of(number_of_guests))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.cannotBeNegative("손님수"));
    }

}
