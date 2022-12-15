package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.*;

@DisplayName("OrderLineItem 클래스 테스트")
class OrderLineItemTest {

    @DisplayName("OrderLineItem 생성한다.")
    @Test
    void successfulCreate() {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1);
        assertThat(orderLineItem).isNotNull();
    }

    @DisplayName("메뉴없이 OrderLineItem 생성한다.")
    @Test
    void failureCreateWithEmptyMenu() {
        assertThatThrownBy(() -> {
            new OrderLineItem(null, 1);
        }).isInstanceOf(NullPointerException.class);
    }

    @DisplayName("주량이 -1인 OrderLineItem 생성한다.")
    @Test
    void failureCreateWithNegativeQuantity() {
        assertThatThrownBy(() -> {
            new OrderLineItem(1L, -1);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
