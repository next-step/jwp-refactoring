package kitchenpos.application;

import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItemDao;
import kitchenpos.table.domain.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.application.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    MenuDao menuDao;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderLineItemDao orderLineItemDao;

    @Mock
    OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    private Long orderTableId = 1L;
    private Long newOrderTableId = 2L;
    private OrderLineItem orderLineItem1;
    private OrderLineItem orderLineItem2;
    private List<OrderLineItem> orderLineItems;
    private Order order;
    private Order chageStatusOrder;
    private Order newOrder;

    @BeforeEach
    void setUp() {
        orderLineItem1 = new OrderLineItem(1L, 1L, 1L, 1L);
        orderLineItem2 = new OrderLineItem(2L, 1L, 1L, 1L);
        orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);
        order = new Order(1L, orderTableId, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        chageStatusOrder = new Order(1L, orderTableId, OrderStatus.MEAL.name(), LocalDateTime.now(), orderLineItems);
        newOrder = new Order(2L, newOrderTableId, OrderStatus.MEAL.name(), LocalDateTime.now(), orderLineItems);
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        given(menuDao.countByIdIn(anyList())).willReturn((long) orderLineItems.size());
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(new OrderTable(1L, null, 0, false)));
        given(orderDao.save(any())).willReturn(order);
        given(orderLineItemDao.save(orderLineItem1)).willReturn(orderLineItem1);
        given(orderLineItemDao.save(orderLineItem2)).willReturn(orderLineItem2);

        Order savedOrder = orderService.create(order);

        assertAll(
                () -> assertThat(savedOrder).isEqualTo(order),
                () -> assertThat(savedOrder.getOrderLineItems()).contains(orderLineItem1, orderLineItem2));
    }

    @DisplayName("주문을 등록을 실패한다 - 주문에 포함된 주문된 메뉴들이(주문 아이템[OrderLineItem]) 없으면 실패한다.")
    @Test
    void fail_create1() {
        Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 등록을 실패한다 - 등록되지 않은 메뉴 주문시 등록 실패한다.")
    @Test
    void fail_create2() {
        Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        given(menuDao.countByIdIn(any())).willReturn((long) orderLineItems.size() - 1);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 등록을 실패한다 - 주문 테이블이 없는 주문은 등록 실패한다.")
    @Test
    void fail_create3() {
        given(menuDao.countByIdIn(any())).willReturn((long) orderLineItems.size());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 등록을 실패한다 - 주문 테이블이 empty (true) 상태면 주문 등록 실패한다.")
    @Test
    void fail_create4() {
        given(menuDao.countByIdIn(any())).willReturn((long) orderLineItems.size());
        given(orderTableDao.findById(any())).willReturn(Optional.of(new OrderTable(1L, 1L, 2, true)));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 조회한다.")
    @Test
    void list() {
        given(orderDao.findAll()).willReturn(Arrays.asList(order, newOrder));

        List<Order> orders = orderService.list();

        assertThat(orders).containsExactly(order, newOrder);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        given(orderDao.findById(anyLong())).willReturn(Optional.ofNullable(chageStatusOrder));
        given(orderLineItemDao.findAllByOrderId(anyLong())).willReturn(orderLineItems);

        Order changed = orderService.changeOrderStatus(this.order.getId(), chageStatusOrder);

        assertAll(
                () -> assertThat(changed).isEqualTo(chageStatusOrder),
                () -> assertThat(changed.getOrderLineItems()).isEqualTo(orderLineItems));
    }

    @DisplayName("주문 상태를 변경을 실패한다 - 기존에 등록된 주문이 없으면 주문 상태 변경에 실패한다.")
    @Test
    void fail_changeOrderStatus1() {
        given(orderDao.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(this.order.getId(), chageStatusOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태를 변경을 실패한다 - 주문 상태가 기존에 계산 완료(COMPLETION) 상태일 경우 주문 상태 변경에 실패한다.")
    @Test
    void fail_changeOrderStatus2() {
        Order order = new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), orderLineItems);
        given(orderDao.findById(anyLong())).willReturn(Optional.ofNullable(order));

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), chageStatusOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
