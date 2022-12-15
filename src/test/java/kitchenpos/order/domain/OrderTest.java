package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Order 클래스 테스트")
class OrderTest {

    private final List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(1L, 1L));

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        Order order = new Order(1L);
        assertAll(
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                () -> assertThat(order.getOrderedTime()).isNotNull()
        );
    }

    @DisplayName("테이블없이 주문을 생성한다.")
    @Test
    void failureWithOrderTable() {
        assertThatThrownBy(() -> {
            new Order(null);
        }).isInstanceOf(NullPointerException.class);
    }

    @DisplayName("주문에 주문 항목을 추가한다.")
    @Test
    void addOrderLineItems() {
        Order order = new Order(1L);

        order.addOrderLineItems(orderLineItems);

        assertAll(
                () -> assertThat(order.getOrderLineItems()).hasSize(1),
                () -> assertThat(order.getOrderLineItems()).element(0)
                                                           .satisfies(it -> {
                                                               assertThat(it.getOrder()).isNotNull();
                                                           })
        );
    }

    @DisplayName("주문에 빈 주문 항목을 추가한다.")
    @Test
    void addOrderLimeItemsWithEmpty() {
        Order order = new Order(1L);

        assertThatThrownBy(() -> {
            order.addOrderLineItems(Collections.emptyList());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        Order order = new Order(1L);

        order.changeOrderStatus(OrderStatus.MEAL);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("주문의 상태를 변경한다.")
    @Test
    void changeOrderStatusWithCompleted() {
        Order order = new Order(1L);
        order.changeOrderStatus(OrderStatus.COMPLETION);

        assertThatThrownBy(() -> {
            order.changeOrderStatus(OrderStatus.COMPLETION);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
