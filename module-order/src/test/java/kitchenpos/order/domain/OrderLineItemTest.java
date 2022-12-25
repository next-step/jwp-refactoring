package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.order.domain.OrderLineItem.MENU_NULL_EXCEPTION_MESSAGE;
import static kitchenpos.order.domain.fixture.OrderLineItemFixture.OrderLineItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 항목")
class OrderLineItemTest {

    @DisplayName("주문 항목을 생성한다.")
    @Test
    void create() {

        OrderLineItem orderLineItem = OrderLineItem();

        assertAll(
                () -> assertThat(orderLineItem.getMenu().getMenuId()).isNotNull(),
                () -> assertThat(orderLineItem.getQuantity()).isNotNull()
        );
    }

    @DisplayName("주문 항목을 생성한다. / 메뉴가 없을 수 없다.")
    @Test
    void create_fail_notMenu() {
        assertThatThrownBy(() -> new OrderLineItem(null, null, new Quantity(1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MENU_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문 항목을 생성한다. / 갯수가 없을 수 없다.")
    @Test
    void create_fail_notQuantity() {
        assertThatThrownBy(() -> new OrderLineItem(null, null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
