package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.testfixtures.MenuTestFixtures;
import kitchenpos.ordertable.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("주문 생성")
    @Test
    void constructor() {
        //given
        OrderTable orderTable = new OrderTable(1L, 6, false);
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(MenuTestFixtures.서비스군만두, 5)
        );

        //when
        Order order = new Order(orderTable, LocalDateTime.now(), orderLineItems);

        //then
        Assertions.assertThat(order.getOrderTable()).isEqualTo(orderTable);
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        //given
        OrderTable orderTable = new OrderTable(1L, 6, false);
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(MenuTestFixtures.서비스군만두, 5)
        );
        Order order = new Order(orderTable, LocalDateTime.now(), orderLineItems);

        order.changeOrderStatus(OrderStatus.MEAL);
        Assertions.assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }
}
