package kitchenpos.application;

import kitchenpos.BaseServiceTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static kitchenpos.utils.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderServiceTest extends BaseServiceTest {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderService orderService;

    private List<OrderLineItem> orderLineItems;

    @BeforeEach
    void beforeSetUp() {
        orderLineItems = Collections.singletonList(OrderLineItem.of(1L, 등록된_menu_id, 2));

        Order order = Order.of(1L, 비어있지_않은_orderTable_id, orderLineItems);
        order.setOrderTableId(비어있지_않은_orderTable_id);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void createOrder() {
        Order order = Order.of(2L, 비어있지_않은_orderTable_id, orderLineItems);
        Order result = orderService.create(order);

        assertThat(result.getOrderTableId()).isEqualTo(비어있지_않은_orderTable_id);
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(result.getOrderedTime()).isNotNull();
        assertThat(result.getOrderLineItems().get(0).getOrderId()).isEqualTo(2L);
    }

    @DisplayName("주문 항목이 하나도 없을 경우 등록할 수 없다.")
    @Test
    void createOrderException1() {
        assertThatThrownBy(() -> orderService.create(Order.of(2L, 비어있지_않은_orderTable_id, null)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("선택한 주문 항목의 메뉴가 등록되어 있지 않으면 등록할 수 없다.")
    @Test
    void createOrderException2() {
        OrderLineItem orderLineItem = OrderLineItem.of(1L, 등록되어_있지_않은_menu_id, 2);
        Order order = Order.of(2L, 비어있지_않은_orderTable_id, Collections.singletonList(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("해당 주문 테이블이 등록되어 있지 않으면 등록할 수 없다.")
    @Test
    void createOrderException3() {
        Order order = Order.of(2L, 등록되어_있지_않은_orderTable_id, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블일 경우 등록할 수 없다.")
    @Test
    void createOrderException4() {
        Order order = Order.of(2L, 빈_orderTable_id, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        Order changeOrder = Order.of(1L, 비어있지_않은_orderTable_id, orderLineItems);
        changeOrder.setOrderStatus(OrderStatus.MEAL.name());

        Order result = orderService.changeOrderStatus(1L, changeOrder);

        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문이 등록되어 있지 않으면 변경할 수 없다.")
    @Test
    void changeOrderStatusException1() {
        Order changeOrder = Order.of(2L, 비어있지_않은_orderTable_id, orderLineItems);
        changeOrder.setOrderStatus(OrderStatus.COOKING.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(2L, changeOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 계산 완료일 경우 변경할 수 없다.")
    @Test
    void changeOrderStatusException2() {
        Order order = orderService.list().get(0);
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        orderDao.save(order);

        Order changeOrder = Order.of(1L,비어있지_않은_orderTable_id, orderLineItems);
        changeOrder.setOrderStatus(OrderStatus.MEAL.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, changeOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
