package kitchenpos.moduledomain.order;

import kitchenpos.moduledomain.common.exception.DomainMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {

    @Test
    void 주문상품_반드시_필수이어야합니다() {
        Assertions.assertThatThrownBy(() -> {
                OrderLineItems.of(null);
            }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(DomainMessage.ORDER_LINE_ITEMS_IS_NOT_NULL.getMessage());
    }
}