package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

import static kitchenpos.utils.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class OrderServiceTest {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderService orderService;

    private Order order;

    @DisplayName("주문을 등록한다.")
    @Test
    void createOrder() {
        order = order_생성(3L, Collections.singletonList(OrderLineItem.of(1L, 4)));
        Order result = orderService.create(order);

        assertThat(result.getOrderTableId()).isEqualTo(3L);
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(result.getOrderedTime()).isNotNull();
        assertThat(result.getOrderLineItems().get(0).getOrderId()).isEqualTo(2L);
    }

    @DisplayName("주문 항목이 하나도 없을 경우 등록할 수 없다.")
    @Test
    void createOrderException1() {
        assertThatThrownBy(() -> orderService.create(order_생성(1L, null)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("선택한 주문 항목의 메뉴가 등록되어 있지 않으면 등록할 수 없다.")
    @Test
    void createOrderException2() {
        order = order_생성(1L, Collections.singletonList(orderLineItem_생성(7L, 2)));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("해당 주문 테이블이 등록되어 있지 않으면 등록할 수 없다.")
    @Test
    void createOrderException3() {
        order = order_생성(4L, Collections.singletonList(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블일 경우 등록할 수 없다.")
    @Test
    void createOrderException4() {
        order = order_생성(1L, Collections.singletonList(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        order = order_생성(3L, Collections.singletonList(OrderLineItem.of(1L, 4)));
        orderService.create(order);
        Order order2 = order_생성(3L, Collections.singletonList(OrderLineItem.of(1L, 4)));
        order2.setOrderStatus(OrderStatus.MEAL.name());

        Order result = orderService.changeOrderStatus(1L, order2);

        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문이 등록되어 있지 않으면 변경할 수 없다.")
    @Test
    void changeOrderStatusException1() {
        order = order_생성(3L, Collections.singletonList(OrderLineItem.of(1L, 4)));
        Order order2 = order_생성(3L, Collections.singletonList(OrderLineItem.of(1L, 4)));
        order2.changeOrderStatus(OrderStatus.COOKING.name());
        //given(orderDao.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(2L, order2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 계산 완료일 경우 변경할 수 없다.")
    @Test
    void changeOrderStatusException2() {
        order = order_생성(3L, Collections.singletonList(OrderLineItem.of(1L, 4)));
//        orderService.create(order);
//        Optional<Order> order1 = orderDao.findById(1L);
//        order1.get().setOrderStatus(OrderStatus.COMPLETION.name());
//        orderDao.save(order1.get());
        order.setOrderedTime(LocalDateTime.now());
        order.changeOrderStatus(OrderStatus.COMPLETION.name());
        orderDao.save(order);
        Order order2 = order_생성(3L, Collections.singletonList(OrderLineItem.of(1L, 4)));
        order2.setOrderStatus(OrderStatus.COMPLETION.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order2))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
