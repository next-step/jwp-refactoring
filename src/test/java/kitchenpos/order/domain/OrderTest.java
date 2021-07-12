package kitchenpos.order.domain;

import kitchenposNew.menu.domain.Menu;
import kitchenposNew.order.domain.Order;
import kitchenposNew.order.domain.OrderLineItem;
import kitchenposNew.order.domain.OrderLineItems;
import kitchenposNew.order.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 생성 테스트")
public class OrderTest {
    @Test
    void 주문_생성_테스트() {
        OrderTable orderTable = new OrderTable();
        OrderLineItem orderLineItem = new OrderLineItem(new Menu(1L),1L);
        Order order = new Order(orderTable, new OrderLineItems(Arrays.asList(orderLineItem)));

        assertThat(order).isEqualTo(new Order(orderTable, new OrderLineItems(Arrays.asList(orderLineItem))));
    }
}
