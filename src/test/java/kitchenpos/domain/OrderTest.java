package kitchenpos.domain;

import kitchenpos.dto.MenuRequest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.MenuTestFixture.*;
import static kitchenpos.fixture.OrderLineItemTestFixture.createOrderLineItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void of() {
        // given
        Long expectedTableId = 1L;
        MenuRequest 짜장면_탕수육_1인_메뉴_세트_요청 = 짜장면_탕수육_1인_메뉴_세트_요청();
        MenuRequest 짬뽕_탕수육_1인_메뉴_세트_요청 = 짬뽕_탕수육_1인_메뉴_세트_요청();
        Menu 짜장면_탕수육_1인_메뉴_세트 = 메뉴_세트_생성(짜장면_탕수육_1인_메뉴_세트_요청);
        Menu 짬뽕_탕수육_1인_메뉴_세트 = 메뉴_세트_생성(짬뽕_탕수육_1인_메뉴_세트_요청);
        OrderLineItem 짜장면_탕수육_1인_메뉴_세트주문 = createOrderLineItem(1L, null, 짜장면_탕수육_1인_메뉴_세트.getId(), 1);
        OrderLineItem 짬뽕_탕수육_1인_메뉴_세트주문 = createOrderLineItem(2L, null, 짬뽕_탕수육_1인_메뉴_세트.getId(), 1);
        List<OrderLineItem> expectedOrderLineItems = Arrays.asList(짜장면_탕수육_1인_메뉴_세트주문, 짬뽕_탕수육_1인_메뉴_세트주문);

        // when
        Order order = Order.of(OrderTable.of(1L, null, 10, false), expectedOrderLineItems);

        // then
        assertAll(
                () -> assertThat(order).isNotNull(),
                () -> assertThat(order.getOrderTable().getId()).isEqualTo(expectedTableId),
                () -> assertThat(order.getOrderLineItems()).containsAll(expectedOrderLineItems)
        );
    }
}
