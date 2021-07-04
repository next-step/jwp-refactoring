package kitchenpos.order.service;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    private String orderStatus;
    private Long orderTableId;
    private Order order;

    @BeforeEach
    public void setup() {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L);
        orderLineItems.add(orderLineItem);
        orderStatus = OrderStatus.COOKING.name();
        orderTableId = 3L;
        order = new Order(orderTableId, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    @Test
    @DisplayName("주문을 생성 한다")
    public void createOrder() {
        // when
        Order createOrder = orderService.create(order);

        // then
        assertThat(createOrder.getOrderTableId()).isEqualTo(orderTableId);
        assertThat(createOrder.getOrderStatus()).isEqualTo(orderStatus);
        assertThat(createOrder.getOrderLineItems()).isNotEmpty();
    }

    @Test
    @DisplayName("주문을 생성 실패 - orderLineItems 가 없을 경우")
    public void createOrderFailByOrderLineItemsIsNull() {
        // given
        Order order = new Order(1L, OrderStatus.COOKING.name(), LocalDateTime.now(), null);

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(order));
    }

    @Test
    @DisplayName("주문 리스트를 가져온다")
    public void selectOrderList() {
        // given
        orderService.create(order);

        // when
        List<Order> orders = orderService.list();

        // then
        for (Order selectOrder : orders) {
            assertThat(selectOrder.getId()).isNotNull();
            assertThat(selectOrder.getOrderTableId()).isNotNull();
            assertThat(selectOrder.getOrderLineItems()).isNotEmpty();
        }
    }

    @Test
    @DisplayName("주문 상태를 변경한다")
    public void modifyOrder() {
        // given
        orderService.create(order);
        String changeStatus = OrderStatus.MEAL.name();
        order.setOrderStatus(changeStatus);

        // when
        Order changeOrder = orderService.changeOrderStatus(3L, this.order);

        // then
        assertThat(changeOrder.getOrderStatus()).isEqualTo(changeStatus);
    }

    @Test
    @DisplayName("주문 상태를 변경 실패 - 이미 계산 완료 된 주문")
    public void modifyOrderFailByCompletionOrder() {
        // given
        order.setOrderStatus(OrderStatus.MEAL.name());

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> orderService.changeOrderStatus(1L, order));
    }

}
