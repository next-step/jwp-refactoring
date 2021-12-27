package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderLineItemsTest {

    @Test
    @DisplayName("주문 항목 리스트 생성")
    public void create() {
        // given
        OrderLineItem 치킨 = new OrderLineItem(1L, 1);
        // when
        OrderLineItems orderLineItems = new OrderLineItems(Collections.singletonList(치킨));
        // then
        assertThat(orderLineItems.getOrderLineItems()).hasSize(1);
        assertThat(orderLineItems.getOrderLineItems()).contains(치킨);
    }
}