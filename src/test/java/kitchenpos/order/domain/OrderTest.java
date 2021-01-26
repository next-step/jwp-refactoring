package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class OrderTest {

    @DisplayName("주문을 생성한다.")
    @Test
    void constructor() {
        // when
        Order order = new Order(
            new OrderTable(10, false),
            OrderStatus.COOKING.name()
        );

        // then
        assertThat(order).isNotNull();
    }

    @DisplayName("주문의 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        Order order = new Order(
            new OrderTable(10, false),
            OrderStatus.COOKING.name()
        );

        // when
        order.changeOrderStatus(OrderStatus.COMPLETION.name());

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }
}
