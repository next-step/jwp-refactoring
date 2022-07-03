package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderLineItemsTest {
    @DisplayName("메뉴 id를 추출할 수 있다")
    @Test
    void extractMenuIds() {
        List<OrderLineItem> orderLineItems = Arrays.asList(
                new OrderLineItem(1L, 1),
                new OrderLineItem(2L, 2),
                new OrderLineItem(3L, 3));

        List<Long> ids = OrderLineItems.extractMenuIds(orderLineItems);

        assertThat(ids).hasSize(3);
        assertThat(ids).containsExactly(1L, 2L, 3L);
    }
}
