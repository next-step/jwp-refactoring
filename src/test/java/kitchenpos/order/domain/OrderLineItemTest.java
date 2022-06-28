package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문항목에 대한 단위 테스트")
class OrderLineItemTest {

    @DisplayName("주문 항목에 주문을 매핑하면 양방향으로 매핑된다")
    @Test
    void map_into_test() {
        // given
        OrderLineItem 주문_항목 = OrderLineItem.of(1L, null, 1L, "test", BigDecimal.valueOf(500L), 1L);
        Order 주문 = Order.of(1L, 1L);

        // when
        주문_항목.mapIntoOrder(주문);

        // then
        assertThat(주문_항목.getOrder()).isEqualTo(주문);
        assertThat(주문.getOrderLineItems()).contains(주문_항목);
    }
}
