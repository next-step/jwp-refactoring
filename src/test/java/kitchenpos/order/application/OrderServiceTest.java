package kitchenpos.order.application;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrderServiceTest {
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderService orderService;

    @DisplayName("1개 이상의 메뉴로 주문 등록")
    @Test
    void createOrder() {
        Menu menu = menuRepository.save(new Menu("메뉴1", Price.of(1000), 1L, null));
        OrderTable orderTable = orderTableRepository.save(new OrderTable(4, true));

        List<OrderLineRequest> orderLinesRequest = Arrays.asList(new OrderLineRequest(menu.getId(), 1L));
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), orderLinesRequest);
        OrderResponse saved = orderService.create(orderRequest);

        assertThat(saved).isNotNull();
    }

    @DisplayName("0개 이하의 메뉴 주문 실패")
    @Test
    void failOrderUnderZeroMenu() {
        OrderRequest orderRequest = new OrderRequest(1L, null);
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블 주문 실패")
    @Test
    void failOrderEmptyTable() {
        OrderRequest orderRequest = new OrderRequest(1L, null);
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 메뉴 주문 실패")
    @Test
    void failOrderNotExiststMenu() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(4, false));

        List<OrderLineRequest> orderLinesRequest = Arrays.asList(new OrderLineRequest(-1L, 1L));
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), orderLinesRequest);

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회")
    @Test
    void list() {
        Menu menu = menuRepository.save(new Menu("메뉴1", Price.of(1000), 1L, null));
        OrderTable orderTable = orderTableRepository.save(new OrderTable(4, false));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(2, false));
        Order order1 = orderRepository.save(new Order(orderTable, Collections.emptyList(), OrderStatus.COOKING));
        Order order2 = orderRepository.save(new Order(orderTable2, Collections.emptyList(), OrderStatus.MEAL));
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(menu, 1L));
        order1.addItems(orderLineItems);
        order2.addItems(orderLineItems);

        List<OrderResponse> results = orderService.list();

        assertThat(results.size()).isGreaterThan(1);
        assertThat(results).contains(OrderResponse.of(order1), OrderResponse.of(order2));
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(4, false));
        Order order = orderRepository.save(new Order(orderTable, Collections.emptyList(), OrderStatus.MEAL));

        OrderResponse savedOrder = orderService.changeOrderStatus(order.getId(), new OrderStatusRequest(OrderStatus.COMPLETION));

        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @DisplayName("주문 상태가 계산 완료인 경우 변경 불가")
    @Test
    void failChangeOrderStatusWhenStatuIsCOMPLETION() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(4, false));

        Order order = orderRepository.save(new Order(orderTable, Collections.emptyList(), OrderStatus.COMPLETION));

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), new OrderStatusRequest(OrderStatus.COOKING)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}