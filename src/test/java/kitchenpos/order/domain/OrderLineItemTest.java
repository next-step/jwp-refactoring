package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.menu.testfixtures.MenuTestFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemTest {


    @DisplayName("주문항목 생성")
    @Test
    void constructor() {
        OrderLineItem orderLineItem = new OrderLineItem(MenuTestFixtures.서비스군만두, new Quantity(5L));
        assertThat(orderLineItem.getMenu()).isEqualTo(MenuTestFixtures.서비스군만두);
    }
}
