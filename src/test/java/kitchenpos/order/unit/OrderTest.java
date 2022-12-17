package kitchenpos.order.unit;

import kitchenpos.order.constant.OrderStatus;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("주문기능")
public class OrderTest {

    @Test
    @DisplayName("주문의 주문 테이블검증 기능 (주문 테이블은 비어있으면 안된다.)")
    void orderTest1() {
        OrderTable orderTable = OrderTable.create(null, 5, true);

        Order order = Order.create(orderTable, null, LocalDateTime.now());

        assertThatThrownBy(() -> order.validate()).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문의 상태변경 기능 (주문 테이블은 비어있으면 안된다.)")
    void orderTest2() {
        Order order = Order.create(null, OrderStatus.COMPLETION.name(), LocalDateTime.now());

        assertThatThrownBy(() -> order.changeStatus(OrderStatus.COMPLETION.name())).isInstanceOf(IllegalArgumentException.class);
    }

}
