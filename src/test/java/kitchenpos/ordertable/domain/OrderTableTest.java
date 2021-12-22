package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("주문 테이블 생성")
    @Test
    void constructor() {
        OrderTable orderTable = new OrderTable(6, false);
        OrderTable expectedTable = new OrderTable(6, false);
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(expectedTable.getNumberOfGuests());
    }

}
