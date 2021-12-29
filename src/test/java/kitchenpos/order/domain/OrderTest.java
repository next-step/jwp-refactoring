package kitchenpos.order.domain;


import static common.MenuFixture.메뉴_양념치킨;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import common.OrderTableFixture;
import java.util.List;
import kitchenpos.common.exception.Message;
import kitchenpos.menu.domain.Menu;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void 주문생성시_주문테이블은_반드시_필수() {
        assertThatThrownBy(() -> {
            Order.createCook(null, null);
        }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(Message.ORDER_TABLE_IS_NOT_NULL.getMessage());
    }

    @Test
    void 주문_정상생성() {
        OrderTable 첫번째_주문테이블 = OrderTableFixture.첫번째_주문테이블();
        Menu 메뉴_양념치킨 = 메뉴_양념치킨();
        OrderLineItem orderLineItem = OrderLineItem.of(메뉴_양념치킨.getId(), 1);
        List<OrderLineItem> orderLineItems = asList(orderLineItem);

        Order cook = Order.createCook(첫번째_주문테이블.getId(), orderLineItems);

        assertThat(cook).isNotNull();
        assertAll(() -> {
            assertThat(cook.getOrderTableId()).isEqualTo(첫번째_주문테이블.getId());
            assertThat(cook.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
            assertThat(cook.getOrderLineItems()).isEqualTo(orderLineItems);
            assertThat(cook.getOrderedTime()).isNotNull();
        });
    }
}