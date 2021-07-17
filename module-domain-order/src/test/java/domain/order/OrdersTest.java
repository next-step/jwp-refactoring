package domain.order;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrdersTest {

    private Order order1;
    private Order order2;

    @BeforeEach
    void setUp() {
        OrderLineItem orderLineItem1 = OrderLineItem.of(1L, 1);
        OrderLineItem orderLineItem2 = OrderLineItem.of(2L, 2);
        order1 = Order.of(1L, OrderStatus.COOKING, OrderLineItems.of(Lists.list(orderLineItem1)));
        order2 = Order.of(1L, OrderStatus.COOKING, OrderLineItems.of(Lists.list(orderLineItem2)));
    }

    @Test
    void create() {
        //when
        Orders orders = Orders.of(Lists.list(order1, order2));

        //then
        assertThat(orders.getUnmodifiableList()).contains(order1, order2);
    }

    @Test
    void getUnmodifiableList() {
        //given
        Orders orders = Orders.of(Lists.list(order1, order2));

        //when
        List<Order> actual = orders.getUnmodifiableList();

        //then
        assertThat(actual).contains(order1, order2);
    }

    @DisplayName("주문 상태가 요리중이거나 식사중인지 여부를 확인한다.")
    @Test
    void hasCookingOrMeal() {
        //given
        Orders orders = Orders.of(Lists.list(order1, order2));

        //when
        boolean actual = orders.hasCookingOrMeal();

        //then
        assertThat(actual).isTrue();
    }

    @DisplayName("주문 테이블에 주문을 추가할 수 있다.")
    @Test
    void newOrder() {
        //given
        Orders orders = Orders.of(Lists.list(order1, order2));
        OrderLineItem newOrderLineItem1 = OrderLineItem.of(3L, 3);
        OrderLineItem newOrderLineItem2 = OrderLineItem.of(4L, 4);

        //when
        orders.newOrder(1L, OrderLineItems.of(Lists.list(newOrderLineItem1, newOrderLineItem2)));

        //then
        assertThat(orders.getUnmodifiableList().size()).isEqualTo(3);
    }
}
