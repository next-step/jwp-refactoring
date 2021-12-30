package kitchenpos.moduledomain.order;


import static java.util.Arrays.asList;
import static kitchenpos.moduledomain.common.MenuFixture.메뉴_양념치킨;
import static kitchenpos.moduledomain.common.OrderFixture.계산_완료;
import static kitchenpos.moduledomain.common.OrderFixture.주문;
import static kitchenpos.moduledomain.common.OrderTableFixture.첫번째_주문테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import kitchenpos.moduledomain.common.exception.Message;
import kitchenpos.moduledomain.menu.Menu;
import kitchenpos.moduledomain.table.OrderTable;
import org.junit.jupiter.api.Assertions;
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
        OrderTable 첫번째_주문테이블 = 첫번째_주문테이블();
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

    @Test
    void 주문상태변경시_이미계산완료_상태라면_예외() {
        // then
        assertThatThrownBy(() -> {
            Order 계산_완료 = 계산_완료();
            계산_완료.changeOrderStatus(OrderStatus.COMPLETION.name());
        }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(Message.ORDER_STATUS_IS_NOT_COMPLETION.getMessage());
    }


    @Test
    void 주문상태변경() {
        // given
        Order 주문 = 주문();

        // when
        주문.changeOrderStatus(OrderStatus.MEAL.name());

        // then
        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }
}