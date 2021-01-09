package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-10
 */
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @DisplayName("주문 생성시 주문항목이 없는 경우")
    @Test
    void orderCreateWithOrderLineItemsTest() {
        Order order = getOrder();
        order.setOrderLineItems(Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성시 중복되는 주문항목이 있는 경우")
    @Test
    void orderCreateWithDuplicateOrderLineItemsTest() {
        Order order = getOrder();
        List<OrderLineItem> orderLineItems = order.getOrderLineItems();
        orderLineItems.add(orderLineItems.get(0));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성시 등록되지 않은 주문항목이 있는 경우")
    @Test
    void orderCreateWithNotRegisteredOrderLineItemsTest() {
        Order order = getOrder();

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성시 테이블이 공석이 아닌 경우")
    @Test
    void orderCreateWithNotEmptyTableTest() {
        Order order = getOrder();

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);

    }

    private Order getOrder() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1);
        ArrayList<OrderLineItem> list = new ArrayList<>();
        list.add(orderLineItem);

        Order order = new Order();
        order.setOrderTableId(7L);
        order.setOrderLineItems(list);
        return order;
    }
}
