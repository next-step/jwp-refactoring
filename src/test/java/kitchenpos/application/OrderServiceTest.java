package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuDao menuDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private OrderService orderService;

    private Order 주문1;
    private Order 주문2;
    private OrderTable 주문테이블;
    private OrderLineItem 주문항목;

    @BeforeEach
    void setUp() {
        주문테이블 = OrderTable.of(1L, null, 2, false);
        주문항목 = OrderLineItem.of(1L, 1L, 1L, 1);
        주문1 = Order.of(1L, 주문테이블.getId(), null, null, Collections.singletonList(주문항목));
        주문2 = Order.of(2L, 2L, null, null, Collections.singletonList(주문항목));
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void 주문_생성() {
        // given
        List<Long> menuIds = 주문1.getOrderLineItems().stream()
                        .map(OrderLineItem::getMenuId)
                        .collect(Collectors.toList());
        when(menuDao.countByIdIn(menuIds)).thenReturn((long) menuIds.size());
        when(orderTableDao.findById(주문1.getOrderTableId())).thenReturn(Optional.of(주문테이블));
        when(orderDao.save(주문1)).thenReturn(주문1);
        when(orderLineItemDao.save(주문항목)).thenReturn(주문항목);

        // when
        Order savedOrder = orderService.create(주문1);

        // then
        assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder.getOrderTableId()).isEqualTo(주문1.getOrderTableId()),
                () -> assertThat(savedOrder.getOrderLineItems()).isEqualTo(주문1.getOrderLineItems())
        );
    }

    @DisplayName("항목이 0개 이하이면 주문을 생성할 수 없다.")
    @Test
    void 항목_0개_주문_생성() {
        // given
        List<Long> menuIds = 주문1.getOrderLineItems().stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        // given
        Order order = Order.of(1L, 주문테이블.getId(), null, null, Collections.emptyList());

        // when / then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("등록되지 않은 메뉴로 주문을 생성할 수 없다.")
    @Test
    void 등록되지않은_메뉴_주문_생성() {
        // given
        List<Long> menuIds = 주문1.getOrderLineItems().stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        when(menuDao.countByIdIn(menuIds)).thenReturn(0L);

        // when / then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(주문1));
    }

    @DisplayName("등록되지 않은 테이블은 주문을 생성할 수 없다.")
    @Test
    void 등록되지않은_테이블_주문_생성() {
        // given
        Order order = Order.of(1L, 주문테이블.getId(), null, null, Collections.singletonList(주문항목));
        List<Long> menuIds = order.getOrderLineItems().stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        when(menuDao.countByIdIn(menuIds)).thenReturn((long) menuIds.size());

        // when / then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("빈 테이블은 주문을 생성할 수 없다.")
    @Test
    void 빈_테이블_주문_생성() {
        // given
        OrderTable 빈테이블 = OrderTable.of(2L, null, 0, true);
        Order order = Order.of(1L, 빈테이블.getId(), null, null, Collections.singletonList(주문항목));
        List<Long> menuIds = order.getOrderLineItems().stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        when(menuDao.countByIdIn(menuIds)).thenReturn((long) menuIds.size());
        when(orderTableDao.findById(order.getOrderTableId())).thenReturn(Optional.of(빈테이블));

        // when / then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void 주문_목록_조회() {
        // given
        List<Order> orders = Arrays.asList(주문1, 주문2);
        when(orderService.list()).thenReturn(orders);

        // when
        List<Order> selectOrders = orderService.list();

        // then
        assertAll(
                () -> assertThat(selectOrders).hasSize(orders.size()),
                () -> assertThat(selectOrders).isEqualTo(orders)
        );
    }

    @DisplayName("주문의 상태를 변경한다.")
    @Test
    void 주문_상태_변경() {
        // given
        String orderStatus = OrderStatus.MEAL.name();
        Order 주문3 = Order.of(주문1.getId(), 주문1.getOrderTableId(), orderStatus, 주문1.getOrderedTime(), 주문1.getOrderLineItems());
        when(orderDao.findById(주문1.getId())).thenReturn(Optional.of(주문1));
        when(orderLineItemDao.findAllByOrderId(주문1.getId())).thenReturn(주문1.getOrderLineItems());
        when(orderDao.save(주문1)).thenReturn(주문1);

        // when
        Order updatedOrder = orderService.changeOrderStatus(주문1.getId(), 주문3);

        // then
        assertThat(updatedOrder.getOrderStatus()).isEqualTo(orderStatus);
    }

    @DisplayName("기등록된 주문이 아니면 주문의 상태를 변경할 수 없다.")
    @Test
    void 등록되지않은_주문_상태_변경() {
        // given
        String orderStatus = OrderStatus.MEAL.name();
        Order 주문3 = Order.of(주문1.getId(), 주문1.getOrderTableId(), orderStatus, 주문1.getOrderedTime(), 주문1.getOrderLineItems());

        // when / then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(주문1.getId(), 주문3));
    }

    @DisplayName("완료 상태 주문은 상태를 변경할 수 없다.")
    @Test
    void 완료_주문_상태_변경() {
        // given
        Order order = Order.of(3L, 3L, OrderStatus.COMPLETION.name(), null, Collections.singletonList(주문항목));
        Order updateOrder = Order.of(order.getId(), order.getOrderTableId(), OrderStatus.MEAL.name(), order.getOrderedTime(), order.getOrderLineItems());

        // when / then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(order.getId(), updateOrder));
    }
}
