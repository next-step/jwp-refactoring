package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문들 관련 Domain 단위 테스트")
class OrdersTest {

    @DisplayName("주문들에 대한 주문상태가 하나라도 포함되어 있는지 확인한다.")
    @Test
    void isTargetOrderStatusAtLeastOne() {
        //given
        Orders orders = new Orders();
        orders.addOrder(new Order(null, OrderStatus.MEAL, null));
        orders.addOrder(new Order(null, OrderStatus.COOKING, null));
        orders.addOrder(new Order(null, OrderStatus.MEAL, null));

        //when
        boolean targetOrderStatusAtLeastOne1 = orders
                .isTargetOrderStatusAtLeastOne(Arrays.asList(OrderStatus.MEAL, OrderStatus.COOKING));
        boolean targetOrderStatusAtLeastOne2 = orders
                .isTargetOrderStatusAtLeastOne(Arrays.asList(OrderStatus.COOKING));
        boolean targetOrderStatusAtLeastOne3 = orders
                .isTargetOrderStatusAtLeastOne(Arrays.asList(OrderStatus.COMPLETION));

        //then
        assertThat(targetOrderStatusAtLeastOne1).isTrue();
        assertThat(targetOrderStatusAtLeastOne2).isTrue();
        assertThat(targetOrderStatusAtLeastOne3).isFalse();
    }
}
