package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderLineItemTest {

    @DisplayName("주문정보 생성 작업을 성공한다.")
    @Test
    void of() {
        // given
        long expectedMenuId = 1L;
        long expectedQuantity = 1L;
        OrderLineItem orderLineItem = OrderLineItem.of(expectedMenuId, expectedQuantity);

        // when & then
        assertAll(
                () -> assertThat(orderLineItem.getMenuId()).isEqualTo(expectedMenuId),
                () -> assertThat(orderLineItem.getQuantity()).isEqualTo(expectedQuantity)
        );
    }
}
