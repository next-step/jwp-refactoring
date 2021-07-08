package kitchenpos.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderLineItemsTest {
    @DisplayName("주문항목이 비어있는지 확인")
    @Test
    void 주문항목이_비어있는지_확인() {
        //given
        OrderLineItems orderLineItems = new OrderLineItems(Collections.emptyList());

        //when, then
        assertThatThrownBy(orderLineItems::validateOrderLineItemsEmpty)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문항목의 숫자와 메뉴 숫자가 같은지 확인")
    @Test
    void 주문항목의_숫자와_메뉴_숫자가_같은지_확인() {
        //given
        OrderLineItems orderLineItems = new OrderLineItems(
                Arrays.asList(
                        new OrderLineItem(1L, new Order(), 1L, 1L),
                        new OrderLineItem(2L, new Order(), 2L, 1L)
                )
        );

        //when, then
        assertThatThrownBy(() -> orderLineItems.validateSizeAndMenuCountDifferent(3))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
