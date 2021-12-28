package kitchenpos.order.domain;

import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {

    @DisplayName("주문 상태 변경")
    @Test
    void updateOrderStatus() {
        OrderTable orderTable = OrderTableFixture.생성(7, true);
        Order order = OrderFixture.생성(orderTable);

        order.updateOrderStatus(OrderStatus.MEAL);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("계산 완료인 상태는 변경 할 수 없다.")
    @Test
    void updateOrderStatusError() {
        OrderTable orderTable = OrderTableFixture.생성(7, true);
        Order order = OrderFixture.생성(orderTable);
        order.updateOrderStatus(OrderStatus.COMPLETION);

        assertThatThrownBy(
                () -> order.updateOrderStatus(OrderStatus.COMPLETION)
        ).isInstanceOf(IllegalArgumentException.class);
    }


}
