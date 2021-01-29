package kitchenpos.order.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class OrderTest {

    private OrderLineItem orderLineItem1;
    private OrderLineItem orderLineItem2;
    private OrderLineItem orderLineItem3;
    private List<OrderLineItem> OrderItemList = new ArrayList<>();


    @BeforeEach
    void setUp() {
        orderLineItem1 = new OrderLineItem(1L, 1L, 1L, 3);
        orderLineItem2 = new OrderLineItem(2L, 1L, 2L, 2);
        orderLineItem3 = new OrderLineItem(3L, 2L, 3L, 1);
        OrderItemList.add(orderLineItem1);
        OrderItemList.add(orderLineItem2);
        OrderItemList.add(orderLineItem3);

    }

    @Test
    @DisplayName("주문항목 메뉴 ID 가져오기")
    void getMenuIds1() {
        Order order = new Order(OrderItemList);

        assertThat(order.getMenuIds(OrderItemList)).isNotNull();
        assertThat(order.getMenuIds(OrderItemList).size()).isEqualTo(3);

    }

    @Test
    @DisplayName("주문항목 메뉴 ID 가져오기 : 주문항목이 비어있으면 등록할 수 없음")
    void getMenuIds2() {
        Order order = new Order();

        assertThatThrownBy(() -> {
            order.getMenuIds(order.getOrderLineItems());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
