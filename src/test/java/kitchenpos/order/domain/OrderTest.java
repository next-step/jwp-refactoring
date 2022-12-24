package kitchenpos.order.domain;

import kitchenpos.common.vo.Name;
import kitchenpos.common.vo.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static kitchenpos.common.fixture.QuantityFixture.quantityOrderLineItemA;
import static kitchenpos.order.application.OrderService.ORDER_LINE_ITEMS_EMPTY_EXCEPTION_MESSAGE;
import static kitchenpos.order.domain.Order.COMPLETION_CHANGE_EXCEPTION_MESSAGE;
import static kitchenpos.order.domain.Order.ORDER_TABLE_NULL_EXCEPTION_MESSAGE;
import static kitchenpos.order.domain.fixture.OrderLineItemsFixture.orderLineItemsA;
import static kitchenpos.table.domain.fixture.OrderTableFixture.orderTableA;
import static org.assertj.core.api.Assertions.*;

@DisplayName("주문")
class OrderTest {

    @DisplayName("주문 항목이 비어있을 수 없다.")
    @Test
    void constructor_fail_orderItem() {
        assertThatThrownBy(() -> new Order(orderTableA().getId(), new OrderLineItems()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_LINE_ITEMS_EMPTY_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문 테이블은 비어있을 수 없다.")
    @Test
    void constructor_fail_orderTable() {
        assertThatThrownBy(() -> new Order(null, new OrderLineItems(Collections.singletonList(new OrderLineItem(null, OrderMenu.of(1L, new Name("a"), new Price(BigDecimal.ONE)), quantityOrderLineItemA())))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_TABLE_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void name() {
        assertThatNoException().isThrownBy(() -> new Order(orderTableA().getId(), orderLineItemsA(OrderMenu.of(1L, new Name("a"), new Price(BigDecimal.ONE)))));
    }

    @DisplayName("주문상태를 식사중으로 변경한다.")
    @Test
    void changeMeal_success() {
        Order order = new Order(orderTableA().getId(), new OrderLineItems(Collections.singletonList(new OrderLineItem(null, OrderMenu.of(1L, new Name("a"), new Price(BigDecimal.ONE)), quantityOrderLineItemA()))));
        order.meal();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("주문완료일 경우 주문상태를 변경할 수 없다.")
    @Test
    void changeMeal_fail_completion() {
        Order order = new Order(orderTableA().getId(), new OrderLineItems(Collections.singletonList(new OrderLineItem(null, OrderMenu.of(1L, new Name("a"), new Price(BigDecimal.ONE)), quantityOrderLineItemA()))));
        order.complete();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
        assertThatThrownBy(order::meal)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(COMPLETION_CHANGE_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문상태를 완료로 변경한다.")
    @Test
    void nameCompletion() {
        Order order = new Order(orderTableA().getId(), new OrderLineItems(Collections.singletonList(new OrderLineItem(null, OrderMenu.of(1L, new Name("a"), new Price(BigDecimal.ONE)), quantityOrderLineItemA()))));
        order.complete();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }
}
