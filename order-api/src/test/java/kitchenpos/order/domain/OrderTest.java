package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.menu.fixture.MenuTestFixture.*;
import static kitchenpos.order.fixture.OrderLineItemTestFixture.주문정보;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTest {

    @DisplayName("주문 생성 작업을 성공한다.")
    @Test
    void of() {
        // given
        MenuRequest 짜장면_탕수육_1인_메뉴_세트_요청 = 짜장면_탕수육_1인_메뉴_세트_요청();
        MenuRequest 짬뽕_탕수육_1인_메뉴_세트_요청 = 짬뽕_탕수육_1인_메뉴_세트_요청();
        Menu 짜장면_탕수육_1인_메뉴_세트 = 메뉴세트(짜장면_탕수육_1인_메뉴_세트_요청, 1L);
        Menu 짬뽕_탕수육_1인_메뉴_세트 = 메뉴세트(짬뽕_탕수육_1인_메뉴_세트_요청, 2L);
        OrderLineItem 짜장면_탕수육_1인_메뉴_세트주문 = 주문정보(짜장면_탕수육_1인_메뉴_세트.getId(), 1);
        OrderLineItem 짬뽕_탕수육_1인_메뉴_세트주문 = 주문정보(짬뽕_탕수육_1인_메뉴_세트.getId(), 1);
        List<OrderLineItem> expectedOrderLineItems = Arrays.asList(짜장면_탕수육_1인_메뉴_세트주문, 짬뽕_탕수육_1인_메뉴_세트주문);

        // when
        Order order = Order.of(1L, expectedOrderLineItems);

        // then
        assertAll(
                () -> assertThat(order).isNotNull(),
                () -> assertThat(order.getOrderLineItems()).containsAll(expectedOrderLineItems)
        );
    }

    @DisplayName("주문 상태 변경 작업을 성공한다.")
    @Test
    void changeOrderStatus() {
        // given
        OrderStatus expectedOrderStatus = OrderStatus.COMPLETION;
        MenuRequest 짜장면_탕수육_1인_메뉴_세트_요청 = 짜장면_탕수육_1인_메뉴_세트_요청();
        MenuRequest 짬뽕_탕수육_1인_메뉴_세트_요청 = 짬뽕_탕수육_1인_메뉴_세트_요청();
        Menu 짜장면_탕수육_1인_메뉴_세트 = 메뉴세트(짜장면_탕수육_1인_메뉴_세트_요청, 1L);
        Menu 짬뽕_탕수육_1인_메뉴_세트 = 메뉴세트(짬뽕_탕수육_1인_메뉴_세트_요청, 2L);
        OrderLineItem 짜장면_탕수육_1인_메뉴_세트주문 = 주문정보(짜장면_탕수육_1인_메뉴_세트.getId(), 1);
        OrderLineItem 짬뽕_탕수육_1인_메뉴_세트주문 = 주문정보(짬뽕_탕수육_1인_메뉴_세트.getId(), 1);
        List<OrderLineItem> expectedOrderLineItems = Arrays.asList(짜장면_탕수육_1인_메뉴_세트주문, 짬뽕_탕수육_1인_메뉴_세트주문);
        Order order = Order.of(1L, expectedOrderLineItems);

        // when
        order.changeOrderStatus(expectedOrderStatus);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(expectedOrderStatus);
    }
}
