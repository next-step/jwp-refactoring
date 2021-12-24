package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {
    @DisplayName("주문을 생성한다")
    @Test
    void testCreate() {
        // given
        OrderTable orderTable = new OrderTable();

        // when
        Order order = Order.create(orderTable);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @DisplayName("주문 테이블이 비어있지 않아야 한다")
    @Test
    void notEmptyTable() {
        // given
        OrderTable orderTable = new OrderTable(3, true);

        // when
        ThrowableAssert.ThrowingCallable callable = () -> Order.create(orderTable);

        // then
        assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
    }
}
