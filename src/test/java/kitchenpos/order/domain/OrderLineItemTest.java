package kitchenpos.order.domain;

import kitchenpos.embeddableEntity.Quantity;
import kitchenpos.order.dto.OrderLineItemRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderLineItemTest {

    @Test
    @DisplayName("OrderLineItem 인스턴스를 생성한다")
    void of() {
        // when
        OrderLineItem orderLineItem = OrderLineItem.of(new OrderLineItemRequest(1L, 10L), new Order());

        // then
        assertAll(
                () -> assertThat(orderLineItem.getMenuId()).isEqualTo(1L),
                () -> assertThat(orderLineItem.getQuantity()).isEqualTo(new Quantity(10L))
        );


    }
}
