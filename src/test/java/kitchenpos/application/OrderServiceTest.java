package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test
    void 주문테이블을_등록할_수_있다() {
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(1L, 1L, 1L, 10),
            new OrderLineItem(2L, 2L, 2L, 10));
        Order order = new Order(1L, 1L, "orderStatus", LocalDateTime.now(), orderLineItems);
        when(menuDao.countByIdIn(Arrays.asList(1L, 2L))).thenReturn(2L);
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(new OrderTable(1L, 1L, 10, false)));
        when(orderDao.save(order)).thenReturn(order);
        when(orderLineItemDao.save(orderLineItems.get(0))).thenReturn(orderLineItems.get(0));
        when(orderLineItemDao.save(orderLineItems.get(1))).thenReturn(orderLineItems.get(1));

        Order savedOrder = orderService.create(order);

        assertThat(savedOrder).isEqualTo(order);
    }

    @Test
    void 주문_항목이_비었으면_주문테이블을_등록할_수_없다() {
        Order order = new Order(1L, 1L, "orderStatus",
            LocalDateTime.now(), Collections.emptyList());

        assertThrows(IllegalArgumentException.class, () -> orderService.create(order));
    }

    @Test
    void 주문_항목_개수와_주문한_메뉴의_개수가_다를_순_없다() {
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(1L, 1L, 1L, 10),
            new OrderLineItem(2L, 2L, 2L, 10));
        Order order = new Order(1L, 1L, "orderStatus", LocalDateTime.now(), orderLineItems);
        when(menuDao.countByIdIn(Arrays.asList(1L, 2L))).thenReturn(3L);

        assertThrows(IllegalArgumentException.class, () -> orderService.create(order));
    }

    @Test
    void 주문테이블_목록을_조회할_수_있다() {
        List<Order> orders = Arrays.asList(
            new Order(1L, 1L, "orderStatus", LocalDateTime.now(), Collections.emptyList()),
            new Order(2L, 2L, "orderStatus", LocalDateTime.now(), Collections.emptyList()));
        when(orderDao.findAll()).thenReturn(orders);
        when(orderLineItemDao.findAllByOrderId(1L))
            .thenReturn(Collections.singletonList(new OrderLineItem()));

        List<Order> listOrders = orderService.list();

        assertThat(listOrders).isEqualTo(orders);
    }

    @Test
    void 주문테이블의_주문_상태를_변경할_수_있다() {
        Order order = new Order(1L, 1L, "COOKING", LocalDateTime.now(),
            Collections.emptyList());
        when(orderDao.findById(1L)).thenReturn(Optional.of(order));
        when(orderLineItemDao.findAllByOrderId(1L)).thenReturn(
            Collections.singletonList(new OrderLineItem()));

        Order changedOrder = orderService.changeOrderStatus(1L, order);

        assertThat(changedOrder).isEqualTo(order);
    }

    @Test
    void 주문테이블의_주문_상태를_완료로_변경할_수_없다() {
        Order order = new Order(1L, 1L, "COMPLETION", LocalDateTime.now(),
            Collections.emptyList());
        when(orderDao.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(IllegalArgumentException.class,
            () -> orderService.changeOrderStatus(1L, order));
    }
}
