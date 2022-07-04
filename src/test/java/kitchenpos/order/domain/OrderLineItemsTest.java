package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;
import kitchenpos.common.domain.Quantity;
import kitchenpos.order.dto.OrderLineItemResponse;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {
    @Test
    void 메뉴와_메뉴_상품들_간의_연관관계를_설정할_수_있어야_한다() {
        // given
        final OrderLineItems orderLineItems = new OrderLineItems();
        final Order order = new Order(1L, 1L, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        final OrderLineItem orderLineItem1 = new OrderLineItem(1L, new Quantity(10));
        final OrderLineItem orderLineItem2 = new OrderLineItem(2L, new Quantity(20));

        // when
        orderLineItems.makeRelations(order, Arrays.asList(orderLineItem1, orderLineItem2));

        // then
        assertThat(order.getOrderLineItems()
                .stream().map(OrderLineItemResponse::getMenuId).collect(Collectors.toList()))
                .containsExactly(orderLineItem1.getMenuId(), orderLineItem2.getMenuId());
    }
}
