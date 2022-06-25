package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 수량 관련 Domain 단위테스트")
class OrderLineItemQuantityTest {

    @DisplayName("주문 수량은 1개 미만일 수 없다.")
    @Test
    void validate() {
        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new OrderLineItemQuantity(-1));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new OrderLineItemQuantity(null));
    }

}
