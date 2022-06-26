package kitchenpos.domain;

import kitchenpos.exception.InvalidOrderStatusException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderTest {

    @Test
    void create() {
        //given
        OrderTable orderTable = new OrderTable(null, 4, false);
        MenuGroup menuGroup = new MenuGroup("menuGroup");
        Menu menu = new Menu("menu", BigDecimal.valueOf(100), menuGroup);
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(menu, 2), new OrderLineItem(menu, 2));

        //when
        Order order = new Order(orderTable, orderLineItems);

        //then
        assertAll(
                () -> assertEquals(OrderStatus.COOKING, order.getOrderStatus()),
                () -> assertEquals(orderTable.getId(), order.getOrderTable().getId()),
                () -> assertEquals(2, order.getOrderLineItems().size())
        );
    }

    @DisplayName("계산완료 상태 주문의 상태는 변경할 수 없다.")
    @Test
    void changeStatus_fail_statusComplete() {
        //given
        OrderTable orderTable = new OrderTable(null, 4, false);
        MenuGroup menuGroup = new MenuGroup("menuGroup");
        Menu menu = new Menu("menu", BigDecimal.valueOf(100), menuGroup);
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(menu, 2), new OrderLineItem(menu, 2));
        Order order = new Order(1L, orderTable, OrderStatus.COMPLETION, orderLineItems);

        //when //then
        assertThatExceptionOfType(InvalidOrderStatusException.class)
                .isThrownBy(() -> order.changeStatus(OrderStatus.COOKING));
    }
}