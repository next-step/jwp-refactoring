package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 항목")
class OrderLineItemTest {

    @DisplayName("주문 항목을 생성한다.")
    @Test
    void create() {

        OrderLineItem orderLineItem = new OrderLineItem(null, 1L, 3);

        assertAll(
                () -> assertThat(orderLineItem.getMenuId()).isNotNull(),
                () -> assertThat(orderLineItem.getQuantity()).isNotNull()
        );
    }

    @DisplayName("주문 항목을 생성한다. / 메뉴가 없을 수 없다.")
    @Test
    void create_fail_notMenu() {
    }

    @DisplayName("주문 항목을 생성한다. / 갯수가 없을 수 없다.")
    @Test
    void create_fail_notQuantity() {
    }
}
