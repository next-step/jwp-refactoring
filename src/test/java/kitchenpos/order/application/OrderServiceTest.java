package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
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

import java.math.BigDecimal;
import java.util.Arrays;
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
        Menu menu = menuRepository.save(new Menu("메뉴1", new BigDecimal(1000), 1L));
        OrderTable orderTable = orderTableRepository.save(new OrderTable(4, false));

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
        Menu menu = menuRepository.save(new Menu("메뉴1", new BigDecimal(1000), 1L));
        OrderTable orderTable = orderTableRepository.save(new OrderTable(4, false));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(2, false));

        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(menu, 1L));
        Order order1 = new Order(orderTable.getId(), orderLineItems);
        Order order2 = new Order(orderTable2.getId(), orderLineItems);
        Order save1 = orderRepository.save(order1);
        Order save2 = orderRepository.save(order2);

        List<OrderResponse> results = orderService.list();

        assertThat(results).contains(OrderResponse.of(save1), OrderResponse.of(save2));
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        Menu menu = menuRepository.save(new Menu("메뉴1", new BigDecimal(1000), 1L));
        OrderTable orderTable = orderTableRepository.save(new OrderTable(4, false));
        List<OrderLineItem> orderLines = Arrays.asList(new OrderLineItem(menu, 1L));
        Order order = orderRepository.save(new Order(orderTable.getId(), orderLines));

        OrderResponse savedOrder = orderService.changeOrderStatus(order.getId(), new OrderStatusRequest("COMPLETION"));

        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @DisplayName("주문 상태가 계산 완료인 경우 변경 불가")
    @Test
    void failChangeOrderStatusWhenStatuIsCOMPLETION() {
        Menu menu = menuRepository.save(new Menu("메뉴1", new BigDecimal(1000), 1L));
        OrderTable orderTable = orderTableRepository.save(new OrderTable(4, false));
        List<OrderLineItem> orderLines = Arrays.asList(new OrderLineItem(menu, 1L));
        Order order = orderRepository.save(new Order(orderTable.getId(), orderLines));
        orderService.changeOrderStatus(order.getId(), new OrderStatusRequest("COMPLETION"));

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), new OrderStatusRequest("COMPLETION")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}