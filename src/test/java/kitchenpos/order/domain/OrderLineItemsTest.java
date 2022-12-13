package kitchenpos.order.domain;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderLineItemsTest {
    @DisplayName("주문 항목이 비어있으면 예외가 발생한다.")
    @Test
    void emptyException() {
        // when & then
        assertThatThrownBy(() -> new OrderLineItems(new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_LINE_ITEMS_IS_EMPTY.getMessage());
    }

    @DisplayName("주문을 설정할 수 있다.")
    @Test
    void setOrder() {
        // given
        MenuGroup menuGroup = new MenuGroup(1L, new Name("한식"));
        Menu menu = new Menu(new Name("불고기"), new Price(BigDecimal.valueOf(12_000)), menuGroup.getId());

        OrderLineItem orderLineItem = new OrderLineItem(new Quantity(1L), menu);
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(orderLineItem));

        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);
        Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now());

        // when
        orderLineItems.setOrder(order);

        // then
        assertThat(orderLineItems.get().get(0).getOrder()).isEqualTo(order);
    }
}
