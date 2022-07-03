package kitchenpos.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderLineItemRepository orderLineItemDao;
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    private final Long ORDER_ID = 1L;
    private final Long ORDER_TABLE_ID = 1L;

    private OrderLineItem orderLineItemRequest;
    private Order orderRequest;
    private Order order;
    private OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        orderLineItemRequest = new OrderLineItem();
        orderRequest = new Order(ORDER_TABLE_ID, Arrays.asList(orderLineItemRequest));
        order = new Order(ORDER_ID, ORDER_TABLE_ID, OrderStatus.COOKING.name(), Arrays.asList(orderLineItemRequest));
        orderLineItem = new OrderLineItem(order);
    }

    @DisplayName("주문을 생성한다")
    @Test
    void create() {
        given(menuRepository.countByIdIn(any())).willReturn(1L);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(new OrderTable(1L)));
        given(orderRepository.save(orderRequest)).willReturn(order);
        given(orderLineItemDao.save(orderLineItemRequest)).willReturn(orderLineItem);

        Order result = orderService.create(orderRequest);

        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @DisplayName("orderLineItems 비어있다면 주문을 생성할 수 없다.")
    @Test
    void create_empty_orderLineItems() {
        orderRequest = new Order();

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하는 menu가 아니라면 주문을 생성할 수 없다.")
    @Test
    void create_invalid_menuIds() {
        orderRequest = new Order(ORDER_TABLE_ID, Arrays.asList(new OrderLineItem(), new OrderLineItem()));
        given(menuRepository.countByIdIn(any())).willReturn(1L);

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하는 주문 테이블이 아니라면 주문을 생성할 수 없다.")
    @Test
    void create_invalid_orderTableId() {
        given(menuRepository.countByIdIn(any())).willReturn(1L);
        given(orderTableRepository.findById(ORDER_TABLE_ID)).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있으면 주문을 생성할 수 없다.")
    @Test
    void create_orderTable_empty() {
        given(menuRepository.countByIdIn(any())).willReturn(1L);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(new OrderTable(1L, true)));

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회한다")
    @Test
    void list() {
        Order firstOrder = new Order(1L);
        Order secondOrder = new Order(2L);
        List<Order> orders = Arrays.asList(firstOrder, secondOrder);

        given(orderRepository.findAll()).willReturn(orders);
        given(orderLineItemDao.findAllByOrderId(1L))
                .willReturn(Arrays.asList(new OrderLineItem(firstOrder)));
        given(orderLineItemDao.findAllByOrderId(2L))
                .willReturn(Arrays.asList(new OrderLineItem(secondOrder)));

        List<Order> result = orderService.list();

        assertThat(result).hasSize(2);
    }

    @DisplayName("주문 상태를 변경한다")
    @Test
    void changeOrderStatus() {
        given(orderRepository.findById(ORDER_ID)).willReturn(Optional.of(order));
        given(orderLineItemDao.findAllByOrderId(ORDER_ID))
                .willReturn(Arrays.asList(new OrderLineItem(order)));

        Order result = orderService.changeOrderStatus(ORDER_ID, new Order(OrderStatus.COMPLETION.name()));

        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @DisplayName("존재하는 주문이 없다면 주문 상태를 변경할 수 없다")
    @Test
    void changeOrderStatus_invalid_orderId() {
        given(orderRepository.findById(ORDER_ID)).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(ORDER_ID, new Order(OrderStatus.COMPLETION.name())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하는 주문의 상태가 COMPLETION이라면, 주문 상태를 변경할 수 없다")
    @Test
    void changeOrderStatus_invalid_orderStatus() {
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderRepository.findById(ORDER_ID)).willReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.changeOrderStatus(ORDER_ID, new Order(OrderStatus.COMPLETION.name())))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
