package kitchenpos.order.domain;

import kitchenpos.common.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 항목 테스트")
class OrderLineItemTest {

    @DisplayName("주문 항목 생성 성공 테스트")
    @Test
    void instantiate_success() {
        // given
        Long menuId = 1L;
        Quantity quantity = Quantity.of(1);

        // when
        OrderLineItem orderLineItem = OrderLineItem.of(menuId, quantity);

        // then
        assertAll(
                () -> assertThat(orderLineItem).isNotNull()
                , () -> assertThat(orderLineItem.getMenuId()).isEqualTo(menuId)
                , () -> assertThat(orderLineItem.getQuantity()).isEqualTo(quantity)
        );
    }
}
