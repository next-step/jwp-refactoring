package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static kitchenpos.util.TestFixture.주문테이블_1_생성;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OrderLineItem Domain Test")
class OrderLineItemTest {
    @Test
    void changeOrderLineItemTest() {
        // given
        Order order = Order.of(주문테이블_1_생성(), 주문_항목_리스트_생성());
        order.addOrderLineItem(주문_항목_생성());

        // then
        assertThat(order.getOrderLineItems()).hasSize(3);
    }

    public static List<OrderLineItem> 주문_항목_리스트_생성() {
        List<OrderLineItem> list = new ArrayList<>();
        list.add(OrderLineItem.of(1L, 1L));
        list.add(OrderLineItem.of(2L, 1L));
        return list;
    }

    public static OrderLineItem 주문_항목_생성() {
        return OrderLineItem.of(3L, 1L);
    }
}
