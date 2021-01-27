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

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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

    private Order order;
    private OrderLineItem orderLineItem;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable();
        orderTable.setId(1L);

        orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(1L);
        orderLineItem.setOrderId(1L);
        orderLineItem.setMenuId(1L);

        order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderLineItems(Arrays.asList(orderLineItem));
    }

    @Test
    @DisplayName("주문 등록")
    void create() {
        when(menuDao.countByIdIn(any())).thenReturn(1L);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));
        when(orderDao.save(any())).thenReturn(order);
        when(orderLineItemDao.save(any())).thenReturn(orderLineItem);

        assertThat(orderService.create(order)).isNotNull();
    }

    @Test
    @DisplayName("주문 등록시 주문항목이 메뉴의 개수와 같이 않으면 등록 안됨")
    void callException() {
        when(menuDao.countByIdIn(any())).thenReturn(0L);

        assertThatThrownBy(() -> {
            orderService.create(order);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 조회")
    void list() {
        when(orderDao.findAll()).thenReturn(Arrays.asList(order));
        when(orderLineItemDao.findAllByOrderId(any())).thenReturn(Arrays.asList(orderLineItem));

        assertThat(orderService.list()).isNotNull();
    }

    @Test
    @DisplayName("주문 수정")
    void changeOrderStatus() {
        order.setOrderTableId(2L);
        when(orderDao.findById(any())).thenReturn(Optional.of(order));
        when(orderDao.save(any())).thenReturn(order);
        when(orderLineItemDao.findAllByOrderId(any())).thenReturn(Arrays.asList(orderLineItem));

        assertThat(orderService.changeOrderStatus(order.getId(), order));
    }

    @Test
    @DisplayName("주문 수정시 주문상태가 완료이면 수정할 수 없음")
    void callExceptionChangeOrderStatus() {
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        when(orderDao.findById(any())).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(order.getId(), order);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
