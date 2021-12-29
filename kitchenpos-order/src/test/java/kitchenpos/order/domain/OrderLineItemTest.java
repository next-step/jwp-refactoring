package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.menu.MenuTestFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import kitchenpos.common.vo.Quantity;

class OrderLineItemTest {


    @DisplayName("주문항목 생성")
    @Test
    void constructor() {
        OrderLineItem orderLineItem = new OrderLineItem(MenuTestFixtures.서비스군만두.getId(),
            new Quantity(5L));
        assertThat(orderLineItem.getMenuId()).isEqualTo(MenuTestFixtures.서비스군만두.getId());
    }
}
