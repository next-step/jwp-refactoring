package order.domain;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import common.domain.OrderStatus;
import common.exception.InvalidOrderStatusException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void create() {
        //given
        Long orderTableId = 1L;
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(1L, 2), new OrderLineItem(2L, 2));

        //when
        Order order = new Order(orderTableId, orderLineItems);

        //then
        assertAll(
                () -> Assertions.assertEquals(OrderStatus.COOKING, order.getOrderStatus()),
                () -> assertEquals(orderTableId, order.getOrderTableId()),
                () -> assertEquals(2, order.getOrderLineItems().size())
        );
    }

    @DisplayName("계산완료 상태 주문의 상태는 변경할 수 없다.")
    @Test
    void changeStatus_fail_statusComplete() {
        //given
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(1L, 2), new OrderLineItem(2L, 2));
        Order order = new Order(1L, 1L, OrderStatus.COMPLETION, orderLineItems);

        //when //then
        assertThatExceptionOfType(InvalidOrderStatusException.class)
                .isThrownBy(() -> order.changeStatus(OrderStatus.COOKING));
    }
}