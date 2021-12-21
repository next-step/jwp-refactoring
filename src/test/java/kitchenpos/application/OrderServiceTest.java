package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.application.TableServiceTest.createOrderTable;
import static kitchenpos.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("주문 서비스")
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

    private OrderTable 주문테이블;
    private Order 주문;
    private OrderLineItem 주문항목;

    @BeforeEach
    void setUp() {
        주문테이블 = createOrderTable(1L, 2, false);
        주문 = createOrder(1L, 주문테이블.getId(), COOKING, LocalDateTime.now());
        주문항목 = createOrderLineItem(주문.getId(), 1L, 1L);
        주문.setOrderLineItems(Collections.singletonList(주문항목));
    }

    @Test
    @DisplayName("주문을 등록한다.")
    void create() {
        when(menuDao.countByIdIn(anyList())).thenReturn(1L);
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(주문테이블));
        when(orderDao.save(any())).thenReturn(주문);
        when(orderLineItemDao.save(any())).thenReturn(주문항목);

        Order order = orderService.create(주문);

        verify(menuDao, times(1)).countByIdIn(anyList());
        verify(orderTableDao, times(1)).findById(anyLong());
        verify(orderDao, times(1)).save(any(Order.class));
        verify(orderLineItemDao, times(1)).save(any(OrderLineItem.class));
        assertThat(order)
                .extracting("id", "orderTableId", "orderStatus", "orderedTime")
                .containsExactly(주문.getId(), 주문.getOrderTableId(), 주문.getOrderStatus(), 주문.getOrderedTime());
    }

    @Test
    @DisplayName("주문 항목의 목록이 비어있는 경우 예외가 발생한다.")
    void validateOrderLineItemsEmpty() {
        주문.setOrderLineItems(Collections.emptyList());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(주문));
    }

    @Test
    @DisplayName("주문 항목의 메뉴가 기존에 등록된 메뉴가 아닌 경우 예외가 발생한다.")
    void validateOrderLineItems() {
        when(menuDao.countByIdIn(anyList())).thenReturn(0L);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(주문));
        verify(menuDao, times(1)).countByIdIn(anyList());
    }

    @Test
    @DisplayName("주문 테이블이 없는 경우 예외가 발생한다.")
    void validateOrderTable() {
        Order 테이블없는주문 = createOrder(2L, null, COOKING, LocalDateTime.now());
        테이블없는주문.setOrderLineItems(Collections.singletonList(주문항목));

        when(menuDao.countByIdIn(anyList())).thenReturn(1L);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(테이블없는주문));
        verify(menuDao, times(1)).countByIdIn(anyList());
    }

    @Test
    @DisplayName("주문 테이블이 빈 테이블인 경우 예외가 발생한다.")
    void validateOrderTableEmpty() {
        OrderTable 빈테이블 = createOrderTable(2L, 0, true);
        Order 빈테이블주문 = createOrder(2L, 빈테이블.getId(), COOKING, LocalDateTime.now());
        빈테이블주문.setOrderLineItems(Collections.singletonList(주문항목));

        when(menuDao.countByIdIn(anyList())).thenReturn(1L);
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(빈테이블));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(빈테이블주문));
        verify(menuDao, times(1)).countByIdIn(anyList());
        verify(orderTableDao, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    void list() {
        when(orderDao.findAll()).thenReturn(Collections.singletonList(주문));
        when(orderLineItemDao.findAllByOrderId(anyLong()))
                .thenReturn(Collections.singletonList(주문항목));

        List<Order> orders = orderService.list();

        verify(orderDao, times(1)).findAll();
        verify(orderLineItemDao, times(1)).findAllByOrderId(anyLong());
        assertThat(orders).hasSize(1);
        assertThat(orders)
                .extracting("id", "orderTableId", "orderStatus", "orderedTime")
                .containsExactly(
                        tuple(주문.getId(), 주문.getOrderTableId(), 주문.getOrderStatus(), 주문.getOrderedTime())
                );
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        when(orderDao.findById(anyLong())).thenReturn(Optional.of(주문));
        when(orderDao.save(any())).thenReturn(주문);

        주문.setOrderStatus(MEAL.name());
        Order order = orderService.changeOrderStatus(주문.getId(), 주문);

        verify(orderDao, times(1)).findById(anyLong());
        verify(orderDao, times(1)).save(any(Order.class));
        assertThat(order.getOrderStatus()).isEqualTo(MEAL.name());
    }

    @Test
    @DisplayName("주문 번호를 조회할 수 없는 경우 예외가 발생한다.")
    void validateOrderId() {
        when(orderDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문));
        verify(orderDao, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("주문 상태가 완료인 경우 예외가 발생한다.")
    void validateOrderStatus() {
        when(orderDao.findById(anyLong())).thenReturn(Optional.of(주문));
        주문.setOrderStatus(COMPLETION.name());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문));
        verify(orderDao, times(1)).findById(anyLong());
    }

    private Order createOrder(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime) {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus.name());
        order.setOrderedTime(orderedTime);
        return order;
    }

    private OrderLineItem createOrderLineItem(Long orderId, Long menuId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}