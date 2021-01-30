package order;

import kitchenpos.menu.Menu;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
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
        Menu menu1 = new Menu("후라이드+후라이드", new BigDecimal(19000));
        Menu menu2 = new Menu("양념반+후라이드반", new BigDecimal(19000));
        Menu menu3 = new Menu("양념반+마늘반", new BigDecimal(19000));

        orderLineItem1 = new OrderLineItem(1L, menu1, 3);
        orderLineItem2 = new OrderLineItem(2L, menu2, 2);
        orderLineItem3 = new OrderLineItem(3L, menu3, 1);
        OrderItemList.add(orderLineItem1);
        OrderItemList.add(orderLineItem2);
        OrderItemList.add(orderLineItem3);

    }

    @Test
    @DisplayName("주문항목 메뉴 ID 가져오기")
    void getMenuIds1() {
        Order order = new Order(OrderItemList);

        assertThat(order.getMenu(OrderItemList)).isNotNull();
        assertThat(order.getMenu(OrderItemList).size()).isEqualTo(3);

    }
}
