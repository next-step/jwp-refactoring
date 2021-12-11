package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문")
class OrderTest {

    @Test
    @DisplayName("생성")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Order.of(1L,
                OrderLineItems.singleton(OrderLineItem.of(1L, 1L))));
    }

    @Test
    @DisplayName("주문 항목들은 필수")
    void instance_nullOrderLineItem_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Order.of(1L, null))
            .withMessage("주문 항목들은 필수입니다.");
    }
}
