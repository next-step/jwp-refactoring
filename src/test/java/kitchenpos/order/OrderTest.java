package kitchenpos.order;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {

    @DisplayName("생성 테스트")
    @Test
    public void createTest() {
        OrderTable orderTable = new OrderTable(0, false);
        List<OrderLineItem> orderLineItems = new ArrayList<OrderLineItem>() {{
            add(new OrderLineItem(new Menu("이름", BigDecimal.ONE, new MenuGroup(), Collections.emptyList()), 1));
        }};
        Order order = new Order(orderTable);
        order.addOrderLineItems(orderLineItems);
        assertThat(order.getOrderedTime()).isNotNull();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(order.getOrderLineItems()).isNotNull();
        assertThat(order.getOrderTable()).isNotNull();
    }

    @DisplayName("주문 등록 불가능한 케이스 1 - 주문내용이 없는 경우")
    @Test
    public void invalidCreateCase1() {
        OrderTable orderTable = new OrderTable(0, false);
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        Order order = new Order(orderTable);
        assertThatThrownBy(() -> {
            order.addOrderLineItems(orderLineItems);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 불가능한 케이스 2 - 주문한 테이블이 비어있는 경우")
    @Test
    public void invalidCreateCase2() {
        OrderTable orderTable = new OrderTable(0, false);
        orderTable.changeEmpty(true);
        List<OrderLineItem> orderLineItems = new ArrayList<OrderLineItem>() {{
            add(new OrderLineItem(new Menu("이름", BigDecimal.ONE, new MenuGroup(), Collections.emptyList()), 1));
        }};
        assertThatThrownBy(() -> {
            Order order = new Order(orderTable);
            order.addOrderLineItems(orderLineItems);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 변경")
    @Test
    public void test6() {
        Order order = new Order(null);
        order.changeStatus(OrderStatus.MEAL);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("주문 상태 변경 불가능한 케이스 - 이미 계산이 완료된 경우")
    @Test
    public void test8() {
        Order order = new Order(null);
        order.changeStatus(OrderStatus.COMPLETION);
        assertThatThrownBy(() -> {
            order.changeStatus(OrderStatus.COOKING);
        }).isInstanceOf(IllegalArgumentException.class);
    }

}
