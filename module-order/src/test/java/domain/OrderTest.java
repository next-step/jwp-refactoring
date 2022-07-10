package domain;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ErrorCode;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {
    @DisplayName("orderLineItem이 비어있지 않다면 생성할 수 있다.")
    @Test
    void valid_orderLineItem() {
        Order order = new Order(1L, Arrays.asList(new OrderLineItem()));

        assertThat(order).isNotNull();
    }

    @DisplayName("orderLineItem이 비어있다면 생성할 수 없다.")
    @Test
    void invalid_orderLineItem() {
        assertThatThrownBy(() -> new Order(1L, null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.ORDER_LINE_ITEM_EMPTY.getMessage());

    }

    @DisplayName("orderLineItem이 비어있지 않다면 생성할 수 있다.")
    @Test
    void register() {
        Order order = new Order(1L, 1L, OrderStatus.COOKING, Arrays.asList(new OrderLineItem()));
        order.register(new OrderTable(1L), Arrays.asList(new OrderLineItem()));

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @DisplayName("orderLineItem이 비어있다면 생성할 수 없다.")
    @Test
    void register_invalid_orderTable() {
        assertThatThrownBy(() -> {
            Order order = new Order(1L, 1L, OrderStatus.COOKING, Arrays.asList(new OrderLineItem()));
            order.register(new OrderTable(1L, true), Arrays.asList(new OrderLineItem()));
        })
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.ORDER_TABLE_EMPTY.getMessage());
    }
}
