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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    private OrderService orderService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    private Order orderRequest;
    private Order savedOrder;
    private OrderLineItem orderLineItem;
    private List<OrderLineItem> orderLineItems;
    private OrderTable emptyOrderTable;
    private OrderTable fullOrderTable;

    @BeforeEach
    void setup() {
        this.orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

        orderLineItem = new OrderLineItem();
        orderLineItems = Collections.singletonList(orderLineItem);

        emptyOrderTable = new OrderTable();
        emptyOrderTable.setEmpty(true);
        fullOrderTable = new OrderTable();
        fullOrderTable.setEmpty(false);
        fullOrderTable.setId(1L);

        orderRequest = new Order();
        orderRequest.setOrderTableId(1L);
        orderRequest.setOrderLineItems(orderLineItems);

        savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setOrderStatus(OrderStatus.COOKING.name());
        savedOrder.setOrderTableId(fullOrderTable.getId());
        savedOrder.setOrderedTime(LocalDateTime.now());
    }

    @DisplayName("1개 미만의 주문 항목으로 주문할 수 없다.")
    @Test
    void createOrderFailWithNotEnoughOrderLineItemsTest() {
        // given
        Order orderRequest = new Order();
        orderRequest.setOrderLineItems(new ArrayList<>());

        // when, then
        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 없는 주문 항목으로 주문할 수 없다.")
    @Test
    void createOrderFailWithNotExistMenuTest() {
        // given
        given(menuDao.countByIdIn(any())).willReturn(100L);

        // when, then
        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 주문테이블에서 주문할 수 없다.")
    @Test
    void createOrderFailWithNotExistOrderTableTest() {
        // given
        given(menuDao.countByIdIn(any())).willReturn((long) orderLineItems.size());

        // when, then
        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있는 주문 테이블에서 주문할 수 없다.")
    @Test
    void createOrderFailWithEmptyOrderTable() {
        // given
        given(menuDao.countByIdIn(any())).willReturn((long) orderLineItems.size());
        given(orderTableDao.findById(orderRequest.getOrderTableId())).willReturn(Optional.of(emptyOrderTable));

        // when, then
        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문할 수 있다.")
    @Test
    void createOrderTest() {
        // given
        given(menuDao.countByIdIn(any())).willReturn((long) orderLineItems.size());
        given(orderTableDao.findById(orderRequest.getOrderTableId())).willReturn(Optional.of(fullOrderTable));
        given(orderDao.save(orderRequest)).willReturn(savedOrder);
        given(orderLineItemDao.save(any())).willReturn(orderLineItem);

        // when
        Order order = orderService.create(orderRequest);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(order.getOrderTableId()).isEqualTo(fullOrderTable.getId());
        assertThat(order.getOrderedTime()).isNotNull();
        assertThat(order.getOrderLineItems()).hasSize(1);
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void getOrdersTest() {
        // given
        given(orderDao.findAll()).willReturn(Collections.singletonList(savedOrder));
        given(orderLineItemDao.findAllByOrderId(savedOrder.getId()))
                .willReturn(Collections.singletonList(orderLineItem));

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getOrderLineItems()).hasSize(1);
    }
}