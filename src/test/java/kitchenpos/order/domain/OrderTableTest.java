package kitchenpos.order.domain;

import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTableTest {

    @DisplayName("방문한 손님 수 변경")
    @Test
    void updateNumberOfGuests() {
        OrderTable orderTable = OrderTableFixture.생성(7, false);

        orderTable.updateNumberOfGuests(5);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(5);
    }

    @DisplayName("방문한 손님은 0명 이상이어야 한다.")
    @Test
    void validationNumber() {
        OrderTable orderTable = OrderTableFixture.생성(7, false);

        assertThatThrownBy(
                () -> orderTable.updateNumberOfGuests(-1)
        ).isInstanceOf(IllegalArgumentException.class);


    }
}
