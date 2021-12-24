package kitchenpos.order.domain;

import static org.junit.jupiter.api.Assertions.*;

import kitchenpos.common.exception.Message;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {
    @Test
    void 주문상품_반드시_필수이어야합니다() {
        Assertions.assertThatThrownBy(() -> {
                OrderLineItems.of(null);
            }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(Message.ORDER_LINE_ITEMS_IS_NOT_NULL.getMessage());
    }
}