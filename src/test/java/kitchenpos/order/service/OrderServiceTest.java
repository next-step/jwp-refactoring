package kitchenpos.order.service;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
    private OrderRequest order;

    @BeforeEach
    public void setup() {
        List<OrderLineItemRequest> orderLineItems = new ArrayList<>();
        OrderLineItemRequest orderLineItem = new OrderLineItemRequest(1L, 1L, 1L);
        orderLineItems.add(orderLineItem);
        orderStatus = OrderStatus.COOKING.name();
        orderTableId = 3L;
        order = new OrderRequest(orderTableId, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    @Test
    @Transactional
    @DisplayName("주문을 생성 한다")
    public void createOrder() {
        // when
        OrderResponse createOrder = orderService.create(order);

        // then
        assertThat(createOrder.getOrderTableId()).isEqualTo(orderTableId);
        assertThat(createOrder.getOrderStatus()).isEqualTo(orderStatus);
        assertThat(createOrder.getOrderLineItems()).isNotEmpty();
    }

    @Test
    @DisplayName("주문을 생성 실패 - orderLineItems 가 없을 경우")
    public void createOrderFailByOrderLineItemsIsNull() {
        // given
        OrderRequest order = new OrderRequest(1L, OrderStatus.COOKING.name(), LocalDateTime.now(), null);

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
        List<OrderResponse> orders = orderService.list();

        // then
        for (OrderResponse selectOrder : orders) {
            assertThat(selectOrder.getId()).isNotNull();
            assertThat(selectOrder.getOrderTableId()).isNotNull();
            assertThat(selectOrder.getOrderLineItems()).isNotEmpty();
        }
    }

    @Test
    @Transactional
    @DisplayName("주문 상태를 변경한다")
    public void modifyOrder() {
        // given
        OrderResponse orderResponse = orderService.create(order);
        String changeStatus = OrderStatus.MEAL.name();
        order.setOrderStatus(changeStatus);

        // when
        OrderResponse changeOrder = orderService.changeOrderStatus(orderResponse.getId(), order);

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
