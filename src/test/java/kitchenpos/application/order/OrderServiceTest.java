package kitchenpos.application.order;

import kitchenpos.order.application.OrderService;
import kitchenpos.dao.order.OrderDao;
import kitchenpos.dao.order.OrderLineItemDao;
import kitchenpos.dao.table.OrderTableDao;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class OrderServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private OrderService orderService;

    @DisplayName("1개 이상의 메뉴로 주문 등록")
    @Test
    void createOrder() {
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(null, 1L, 10));
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(new OrderTable()));
        given(menuRepository.countByIdIn(any())).willReturn(Long.valueOf(orderLineItems.size()));
        OrderTable orderTable = new OrderTable(2, false);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        Order order = new Order(1L, null, LocalDateTime.now(), orderLineItems);
        given(orderDao.save(any())).willReturn(order);

        Order savedOrder = orderService.create(order);

        assertThat(order).isEqualTo(savedOrder);
    }

    @DisplayName("0개 이하의 메뉴 주문 실패")
    @Test
    void failOrderUnderZeroMenu() {
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(new OrderTable()));
        OrderTable orderTable = new OrderTable(2, false);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        Order order = new Order(1L, null, LocalDateTime.now(), null);
        given(orderDao.save(any())).willReturn(order);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블 주문 실패")
    @Test
    void failOrderEmptyTable() {
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(null, 1L, 10));
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(new OrderTable()));
        given(menuRepository.countByIdIn(any())).willReturn(Long.valueOf(orderLineItems.size()));
        OrderTable orderTable = new OrderTable(2, true);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        Order order = new Order(1L, null, LocalDateTime.now(), orderLineItems);
        given(orderDao.save(any())).willReturn(order);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 메뉴 주문 실패")
    @Test
    void failOrderNotExiststMenu() {
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(null, 1L, 10));
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(new OrderTable()));
        given(menuRepository.countByIdIn(any())).willReturn(0L);
        OrderTable orderTable = new OrderTable(2, false);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        Order order = new Order(1L, null, LocalDateTime.now(), orderLineItems);
        given(orderDao.save(any())).willReturn(order);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회")
    @Test
    void list() {
        List<OrderLineItem> orderLineItems = Arrays.asList(
                new OrderLineItem(null, 1L, 2),
                new OrderLineItem(null, 2L, 1)
        );
        Order order1 = new Order(1L, OrderStatus.COOKING.name(), LocalDateTime.now().minusMinutes(30), orderLineItems);
        Order order2 = new Order(2L, OrderStatus.MEAL.name(), LocalDateTime.now().minusMinutes(40), orderLineItems);
        given(orderDao.findAll()).willReturn(Arrays.asList(order1, order2));
        given(orderLineItemDao.findAllByOrderId(anyLong())).willReturn(orderLineItems);

        List<Order> results = orderService.list();

        assertThat(results).contains(order1, order2);
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now().minusMinutes(30), null);
        given(orderDao.findById(any())).willReturn(Optional.of(order));

        Order savedOrder = orderService.changeOrderStatus(order.getId(), order);

        assertThat(order.getOrderStatus()).isEqualTo(savedOrder.getOrderStatus());
    }

    @DisplayName("주문 상태가 계산 완료인 경우 변경 불가")
    @Test
    void failChangeOrderStatusWhenStatuIsCOMPLETION() {
        Order order = new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now().minusMinutes(30), null);
        given(orderDao.findById(any())).willReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order))
                .isInstanceOf(IllegalArgumentException.class);
    }
}