package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("OrderLineItem 주문 항목 도메인 테스트")
class OrderLineItemTest {

    @DisplayName("주문 항목을 생성할 수 있다.")
    @Test
    void create01() {
        // given & when
        OrderLineItem orderLineItem = OrderLineItem.of(1L, 1L);

        // then
        assertAll(
                () -> assertThat(orderLineItem).isNotNull(),
                () -> assertThat(orderLineItem.findQuantity()).isEqualTo(1)
        );
    }
}
