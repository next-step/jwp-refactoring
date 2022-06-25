package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("계산완료 상태 주문의 상태는 변경할 수 없다.")
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