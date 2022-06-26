package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("주문 객체가 같은지 검증")
    void verifyEqualsOrder() {
        final OrderTable orderTable = new OrderTable(1L, null, 5, false);
        final Orders order = new Orders(1L, orderTable, OrderStatus.COOKING, null, null);

        assertThat(order).isEqualTo(new Orders(1L, orderTable, OrderStatus.COOKING, null, null));
    }
}
