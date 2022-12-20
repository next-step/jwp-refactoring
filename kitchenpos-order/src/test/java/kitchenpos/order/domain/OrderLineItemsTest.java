package kitchenpos.order.domain;

import kitchenpos.TestMenuGroupFactory;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Quantity;
import kitchenpos.fixture.TestMenuFactory;
import kitchenpos.fixture.TestOrderFactory;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderLineItemsTest {
    @DisplayName("주문을 설정할 수 있다.")
    @Test
    void setOrder() {
        // given
        MenuGroup menuGroup = TestMenuGroupFactory.create(1L, "한식");
        Menu menu = TestMenuFactory.create("불고기", BigDecimal.valueOf(12_000), menuGroup.getId(), new ArrayList<>());

        OrderLineItem orderLineItem = new OrderLineItem(new Quantity(1L), menu);
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(orderLineItem));

        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);
        Order order = TestOrderFactory.create(orderTable.getId(), OrderStatus.COOKING, new ArrayList<>());

        // when
        orderLineItems.setOrder(order);

        // then
        assertThat(orderLineItems.get().get(0).getOrder()).isEqualTo(order);
    }
}
