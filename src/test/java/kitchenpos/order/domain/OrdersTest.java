package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 컬렉션 도메인 테스트")
public class OrdersTest {
    @DisplayName("컬렉션에 속한 모든 주문이 완료인지 판단")
    @Test
    void 모든_주문_완료_여부_확인() {
        // given
        OrderTable orderTable = OrderTable.of(5, true);
        Order firstOrder = Order.of(orderTable);
        Order secondOrder = Order.of(orderTable);
        firstOrder.changeOrderStatus(OrderStatus.COMPLETION);
        secondOrder.changeOrderStatus(OrderStatus.COMPLETION);

        Orders orders = new Orders();
        orders.addOrder(firstOrder);
        orders.addOrder(secondOrder);

        // when
        boolean isComplete = orders.isCompleteOrders();

        // then
        assertThat(isComplete).isTrue();
    }
}
