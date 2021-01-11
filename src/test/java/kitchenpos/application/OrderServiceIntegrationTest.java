package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrderServiceIntegrationTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderService orderService;


    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void createOrder() {
        // given
        OrderTable orderTable = tableService.create(new OrderTable(4, false));

        // when
        Order order = orderService.create(new Order(orderTable.getId(), getOrderLineItems()));

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @DisplayName("주문 목록을 가져올 수 있다.")
    @Test
    void findOrderList() {
        // given
        OrderTable orderTable = tableService.create(new OrderTable(4, false));
        orderService.create(new Order(orderTable.getId(), getOrderLineItems()));
        orderService.create(new Order(orderTable.getId(), getOrderLineItems()));

        // when then
        assertThat(orderService.list()).hasSize(2);
    }

    @DisplayName("주문한 메뉴는 1개 이상이어야 한다.")
    @Test
    void expectedEmptyMenu() {
        // given
        OrderTable orderTable = tableService.create(new OrderTable(4, false));

        // when
        assertThatThrownBy(() -> orderService.create(new Order(orderTable.getId(), new ArrayList<>())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문은 테이블이 비어있을때만 가능하다.")
    @Test
    void expectedNotEmptyTable() {
        // given
        OrderTable orderTable = tableService.create(new OrderTable(4, true));

        // when
        assertThatThrownBy(() -> orderService.create(new Order(orderTable.getId(), getOrderLineItems()))).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 완료된 주문은 상태값 변경이 불가능하다.")
    @Test
    void shouldNotChangeCompletedOrder() {
        // given
        OrderTable orderTable = tableService.create(new OrderTable(4, false));
        Order order = orderService.create(new Order(orderTable.getId(), getOrderLineItems()));
        order.setOrderStatus(OrderStatus.COMPLETION.name());

        // when then
        orderService.changeOrderStatus(order.getId(), order);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
        assertThatThrownBy(() -> {
            order.setOrderStatus(OrderStatus.COOKING.name());
            orderService.changeOrderStatus(order.getId(), order);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    private List<OrderLineItem> getOrderLineItems() {
        return Arrays.asList(
                new OrderLineItem(1L, 1),
                new OrderLineItem(2L, 1)
        );
    }
}