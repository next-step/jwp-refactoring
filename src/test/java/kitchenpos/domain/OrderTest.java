package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class OrderTest {

    @Test
    void changeStatus_fail_statusComplete() {
        //given
        OrderTable orderTable = new OrderTable(null, 4, false);
        MenuGroup menuGroup = new MenuGroup("menuGroup");
        Menu menu = new Menu("menu", BigDecimal.valueOf(100), menuGroup);
        Order order = new Order(1L, orderTable, OrderStatus.COMPLETION, Arrays.asList(new OrderLineItem(menu, 2)));

        //when //then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> order.changeStatus(OrderStatus.COOKING));
    }
}