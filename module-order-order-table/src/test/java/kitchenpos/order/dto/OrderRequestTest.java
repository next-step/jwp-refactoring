package kitchenpos.order.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.EmptyOrderLineItemsException;
import kitchenpos.order.dto.OrderRequest;
import org.junit.jupiter.api.Test;

class OrderRequestTest {
    @Test
    void 주문_항목_목록_없는_시_예외() {
        assertThatThrownBy(
                () -> new OrderRequest(1L, null)
        ).isInstanceOf(EmptyOrderLineItemsException.class);
    }
}
