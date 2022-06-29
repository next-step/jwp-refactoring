package kitchenpos.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.Exception.EmptyOrderLineItemsException;
import org.junit.jupiter.api.Test;

class OrderRequestTest {
    @Test
    void 주문_항목_목록_없는_시_예외() {
        assertThatThrownBy(
                () -> new OrderRequest(1L, null)
        ).isInstanceOf(EmptyOrderLineItemsException.class);
    }
}
