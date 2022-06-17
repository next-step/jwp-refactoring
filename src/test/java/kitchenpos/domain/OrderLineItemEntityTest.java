package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("OrderLineItem 클래스 테스트")
class OrderLineItemEntityTest {

    @DisplayName("OrderLineItem 생성한다.")
    @Test
    void successfulCreate() {
        OrderLineItemEntity orderLineItem = new OrderLineItemEntity(1L, 1);
        assertThat(orderLineItem).isNotNull();
    }

    @DisplayName("메뉴없이 OrderLineItem 생성한다.")
    @Test
    void failureCreateWithEmptyMenu() {
        assertThatThrownBy(() -> {
            new OrderLineItemEntity(null, 1);
        }).isInstanceOf(NullPointerException.class);
    }

    @DisplayName("주량이 -1인 OrderLineItem 생성한다.")
    @Test
    void failureCreateWithNegativeQuantity() {
        assertThatThrownBy(() -> {
            new OrderLineItemEntity(1L, -1);
        }).isInstanceOf(InvalidQuantityException.class);
    }
}