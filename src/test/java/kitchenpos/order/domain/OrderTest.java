package kitchenpos.order.domain;


import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import common.MenuFixture;
import common.OrderTableFixture;
import java.util.List;
import kitchenpos.common.exception.Message;
import kitchenpos.table.domain.OrderTable;
import org.assertj.core.api.Assertions;
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
        OrderLineItem orderLineItem = OrderLineItem.of(MenuFixture.메뉴_양념치킨(), 1);
        List<OrderLineItem> orderLineItems = asList(orderLineItem);

        Order cook = Order.createCook(첫번째_주문테이블, orderLineItems);
        
        assertThat(cook).isNotNull();
        assertAll(() -> {
            assertThat(cook.getOrderTable()).isEqualTo(첫번째_주문테이블);
            assertThat(cook.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
            assertThat(cook.getOrderLineItems()).isEqualTo(orderLineItems);
            assertThat(cook.getOrderedTime()).isNotNull();
        });
    }
}