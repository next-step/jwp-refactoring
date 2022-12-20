package kitchenpos.order.domain;

import kitchenpos.exception.ErrorMessage;
import kitchenpos.table.domain.OrderLineItemQuantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("주문 메뉴의 수량을 관리하는 클래스 테스트")
class OrderLineItemQuantityTest {

    @DisplayName("수량에 음수 값을 입력할 수 없다.")
    @Test
    void 수량에_음수_값을_입력할_수_없다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new OrderLineItemQuantity(-1))
                .withMessage(ErrorMessage.ORDER_LINE_ITEM_INVALID_QUANTITY.getMessage());
    }
}
