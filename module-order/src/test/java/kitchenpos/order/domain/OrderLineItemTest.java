package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.math.BigDecimal;
import kitchenpos.common.exception.InvalidArgumentException;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 항목 도메인 테스트")
class OrderLineItemTest {

    @Test
    @DisplayName("동등성 비교")
    void equalsOrderLineItem() {
        OrderLineItem orderLineItem_1 = OrderLineItem.of(
            OrderMenu.of(1L, "후라이드치킨", BigDecimal.valueOf(10000)), 2L);
        OrderLineItem orderLineItem_2 = OrderLineItem.of(
            OrderMenu.of(1L, "후라이드치킨", BigDecimal.valueOf(10000)), 1L);

        assertFalse(orderLineItem_1.equalsOrderLineItem(orderLineItem_2));
    }

    @Test
    @DisplayName("메뉴는 필수")
    void createValidateMenu() {
        assertThatThrownBy(() -> OrderLineItem.of(OrderMenu.of(1L, null, null), 2L))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("메뉴는 필수 입니다.");
    }
}