package kitchenpos.order.domain;

import kitchenpos.order.message.OrderMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    private List<OrderLineItem> orderLineItems;

    @BeforeEach
    void setUp() {
        this.orderLineItems = Arrays.asList(
                OrderLineItem.of(1L, "후라이드치킨1", 16_000L, 1L),
                OrderLineItem.of(2L, "강정치킨", 12_000L, 1L)
        );
    }

    @Test
    @DisplayName("주문 생성에 성공한다")
    void createOrderTest() {
        // when
        Order order = new Order(1L, orderLineItems);

        // then
        assertThat(order).isEqualTo(new Order(1L, orderLineItems));
    }

    @Test
    @DisplayName("주문 생성시 주문 상품이 비어있는 경우 생성에 실패한다")
    void createOrderThrownByEmptyOrderItemsTest() {
        // given
        orderLineItems = new ArrayList<>();

        // when & then
        assertThatThrownBy(() -> new Order(1L, orderLineItems))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderMessage.CREATE_ERROR_ORDER_LINE_ITEMS_IS_EMPTY.message());
    }

    @Test
    @DisplayName("주문의 상태가 조리중 또는 식사중 상태인 경우 [true]를 반환한다")
    void isCookingOrMealStateTest() {
        // given
        Order order = Order.cooking(1L, orderLineItems);

        // when
        boolean orderState = order.isCookingOrMealState();

        // then
        assertThat(orderState).isTrue();
    }

    @Test
    @DisplayName("주문의 상태를 변경한다")
    void changeOrderStateTest() {
        // given
        Order order = Order.cooking(1L, orderLineItems);

        // when
        order.changeState(OrderStatus.COMPLETION);

        // then
        assertThat(order.isCookingOrMealState()).isFalse();
    }
}
