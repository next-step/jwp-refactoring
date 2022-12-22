package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("주문테이블요청검증기능")
public class OrderTableValidatorTest {

    @Test
    @DisplayName("손님 수는 음수여선 안된다.")
    void orderTableValidatorTest1() {
        assertThatThrownBy(() -> OrderTableValidator.validateNumberOfGuests(-1)).isInstanceOf(IllegalArgumentException.class);
    }

}
