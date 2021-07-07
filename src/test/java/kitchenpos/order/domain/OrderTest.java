package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderTest {
    private OrderLineItems orderLineItems;
    private String orderStatus;
    private OrderTable orderTable;

    @BeforeEach
    public void setup() {
        List<OrderLineItem> orderLineItemList = new ArrayList<>();
        Menu menu = new Menu("후라이드치킨", new BigDecimal(16000), new MenuGroup("새로운 메뉴"));
        OrderLineItem orderLineItem = new OrderLineItem(menu, 1L);
        orderLineItemList.add(orderLineItem);
        orderStatus = OrderStatus.COOKING.name();
        orderTable = new OrderTable(3L, 1, false);
        orderLineItems = new OrderLineItems(orderLineItemList);
    }

    @Test
    @DisplayName("주문을 생성 한다")
    public void createOrder() {
        // given
        LocalDateTime orderedTime = LocalDateTime.now();

        // when
        Order order = new Order(orderTable, orderStatus, orderedTime);
        orderLineItems.mappingOrder(order);

        // then
        assertThat(order).isEqualTo(new Order(orderTable, orderStatus, orderedTime));
    }

    @Test
    @DisplayName("주문 상태를 변경한다")
    public void modifyOrder() {
        // given
        Order order = new Order(orderTable, orderStatus, LocalDateTime.now());
        orderLineItems.mappingOrder(order);

        // when
        order.changeOrderStatus(OrderStatus.MEAL.name());

        // then
        assertThat(order.orderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    @DisplayName("주문 상태를 변경 실패 - 이미 계산 완료 된 주문")
    public void modifyOrderFailByCompletionOrder() {
        // given
        Order order = new Order(orderTable, OrderStatus.COMPLETION.name(), LocalDateTime.now());
        orderLineItems.mappingOrder(order);

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> order.changeOrderStatus(OrderStatus.MEAL.name()));
    }
}
