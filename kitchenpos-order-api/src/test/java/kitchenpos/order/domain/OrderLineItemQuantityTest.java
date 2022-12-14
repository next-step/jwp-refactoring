package kitchenpos.order.domain;

import kitchenpos.order.exception.OrderLineItemExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 메뉴의 수량을 관리하는 클래스 테스트")
class OrderLineItemQuantityTest {
    @ParameterizedTest
    @ValueSource(longs = { -1, -5, -10, -15 })
    void 수량에_음수_값을_입력할_수_없음(long quantity) {
        assertThatThrownBy(() -> {
            new OrderLineItemQuantity(quantity);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderLineItemExceptionCode.INVALID_QUANTITY.getMessage());
    }
}
