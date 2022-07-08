package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static kitchenpos.order.domain.OrderTest.주문_생성;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OrderLineItem Domain Test")
class OrderLineItemTest {
    @Test
    void changeOrderLineItemTest() {
        // given
        Order order = 주문_생성();
        order.addOrderLineItem(주문_항목_생성());

        // then
        assertThat(order.getOrderLineItems()).hasSize(3);
    }

    public static OrderLineItem 주문_항목_생성() {
        return OrderLineItem.of(3L, "메뉴1", new BigDecimal(16000), 1L);
    }
}
