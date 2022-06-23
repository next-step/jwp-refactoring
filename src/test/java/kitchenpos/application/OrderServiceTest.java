package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.domain.OrderEntity;
import kitchenpos.order.domain.OrderLineItemEntity;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.request.OrderRequest;
import kitchenpos.order.domain.response.OrderResponse;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.application.OrderService;
import kitchenpos.table.domain.OrderTableEntity;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.request.OrderLineItemRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 서비스에 대한 테스트")
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
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderLineItemRepository orderLineItemRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    private OrderLineItem 주문항목_1;
    private OrderLineItem 주문항목_2;
    private OrderLineItem 주문항목_3;
    private OrderTable 주문_테이블;
    private Order 주문_1;
    private Order 주문_2;

    private OrderTableEntity 주문_테이블_entity;

    private OrderLineItemRequest 주문_항목_request;
    private OrderLineItemRequest 주문_항목_request2;
    private OrderRequest 주문_request;

    private OrderLineItemEntity 주문_항목;
    private OrderLineItemEntity 주문_항목2;
    private OrderEntity 주문;

    @BeforeEach
    void setUp() {
        주문항목_1 = OrderLineItem.of(1L, 1L, 1L, 1);
        주문항목_2 = OrderLineItem.of(2L, 1L, 2L, 1);
        주문항목_3 = OrderLineItem.of(3L, 1L, 3L, 2);
        주문_테이블 = OrderTable.of(1L, null, 3, false);
        주문_1 = Order.of(1L, 주문_테이블.getId(), null, null,
            Arrays.asList(주문항목_1, 주문항목_2, 주문항목_3));
        주문_2 = Order.of(2L, 1L, null, null, null);

        주문_테이블_entity = OrderTableEntity.of(1L, null, 3, false);

        주문_항목_request = new OrderLineItemRequest(1L, 1);
        주문_항목_request2 = new OrderLineItemRequest(2L, 1);
        주문_request = new OrderRequest(1L, null, null, Arrays.asList(주문_항목_request, 주문_항목_request2));

        주문_항목 = OrderLineItemEntity.of(null, null, 주문_항목_request.getMenuId(), 주문_항목_request.getQuantity());
        주문_항목2 = OrderLineItemEntity.of(null, null, 주문_항목_request2.getMenuId(), 주문_항목_request2.getQuantity());
        주문 = OrderEntity.of(1L, 주문_테이블_entity);
    }

    @DisplayName("주문을 등록하면 정상적으로 등록되어야 한다")
    @Test
    void create_test() {
        when(menuDao.countByIdIn(Arrays.asList(
            주문항목_1.getMenuId(), 주문항목_2.getMenuId(), 주문항목_3.getMenuId())))
            .thenReturn(3L);
        when(orderTableDao.findById(주문_1.getOrderTableId()))
            .thenReturn(Optional.of(주문_테이블));
        when(orderDao.save(주문_1))
            .thenReturn(주문_1);
        when(orderLineItemDao.save(주문항목_1))
            .thenReturn(주문항목_1);
        when(orderLineItemDao.save(주문항목_2))
            .thenReturn(주문항목_2);
        when(orderLineItemDao.save(주문항목_3))
            .thenReturn(주문항목_3);

        // when
        Order result = orderService.create(주문_1);

        // then
        assertAll(
            () -> assertThat(result.getId()).isEqualTo(주문_1.getId()),
            () -> assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
            () -> assertNotNull(result.getOrderedTime()),
            () -> assertThat(result.getOrderLineItems()).hasSize(3)
        );
    }

    @DisplayName("주문을 등록하면 정상적으로 등록되어야 한다")
    @Test
    void create_test_copy() {
        // given
        when(menuDao.countByIdIn(Arrays.asList(주문_항목_request.getMenuId(), 주문_항목_request2.getMenuId())))
            .thenReturn(2L);
        when(orderTableRepository.findById(주문_request.getOrderTableId()))
            .thenReturn(Optional.of(주문_테이블_entity));
        when(orderRepository.save(any()))
            .thenReturn(주문);

        // when
        OrderResponse result = orderService.createCopy(주문_request);

        // then
        assertAll(
            () -> assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
            () -> assertNotNull(result.getOrderedTime()),
            () -> assertThat(result.getOrderLineItems()).hasSize(2)
        );
    }

    @DisplayName("주문등록시 주문의 항목이 비어있다면 예외가 발생한다")
    @Test
    void create_exception_test() {
        // given
        주문_1.setOrderLineItems(null);

        // then
        assertThatThrownBy(() -> {
            orderService.create(주문_1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문등록시 주문 항목의 메뉴중 존재하지 않는 메뉴가 있다면 예외가 발생한다")
    @Test
    void create_exception_test2() {
        // given
        when(menuDao.countByIdIn(Arrays.asList(
            주문항목_1.getMenuId(), 주문항목_2.getMenuId(), 주문항목_3.getMenuId())))
            .thenReturn(2L);

        // then
        assertThatThrownBy(() -> {
            orderService.create(주문_1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문등록시 주문 테이블이 존재하지 테이블이라면 예외가 발생한다")
    @Test
    void create_exception_test3() {
        // given
        when(menuDao.countByIdIn(Arrays.asList(
            주문항목_1.getMenuId(), 주문항목_2.getMenuId(), 주문항목_3.getMenuId())))
            .thenReturn(3L);
        when(orderTableDao.findById(주문_1.getOrderTableId()))
            .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> {
            orderService.create(주문_1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문등록시 주문 테이블이 존재하지 테이블이라면 예외가 발생한다")
    @Test
    void create_exception_test4() {
        // given
        주문_테이블.setEmpty(true);

        when(menuDao.countByIdIn(Arrays.asList(
            주문항목_1.getMenuId(), 주문항목_2.getMenuId(), 주문항목_3.getMenuId())))
            .thenReturn(3L);
        when(orderTableDao.findById(주문_1.getOrderTableId()))
            .thenReturn(Optional.of(주문_테이블));

        // then
        assertThatThrownBy(() -> {
            orderService.create(주문_1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문상태를 변경하면 정상적으로 변경되어야 한다")
    @Test
    void change_order_test() {
        // given
        주문_2.setOrderStatus(OrderStatus.COOKING.name());
        when(orderDao.findById(주문_1.getId()))
            .thenReturn(Optional.of(주문_1));
        when(orderDao.save(주문_1))
            .thenReturn(주문_1);
        when(orderLineItemDao.findAllByOrderId(주문_1.getId()))
            .thenReturn(Collections.emptyList());

        // when
        Order result = orderService.changeOrderStatus(주문_1.getId(), 주문_2);

        // then
        assertThat(result.getId()).isEqualTo(주문_1.getId());
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @DisplayName("주문상태를 변경하면 정상적으로 변경되어야 한다")
    @Test
    void change_order_test_copy() {
        // given
        주문_request = new OrderRequest(null, OrderStatus.MEAL, null, null);
        when(orderRepository.findById(주문.getId()))
            .thenReturn(Optional.of(주문));
        when(orderRepository.findAllOrderAndItemsByOrder(주문))
            .thenReturn(주문);

        // when
        OrderResponse result = orderService.changeOrderStatusCopy(주문.getId(), 주문_request);

        // then
        assertThat(result.getId()).isEqualTo(주문.getId());
        assertThat(result.getOrderStatus()).isEqualTo(주문_request.getOrderStatus());
    }

    @DisplayName("주문상태를 변경할 주문이 없다면 예외가 발생한다")
    @Test
    void change_order_exception_test() {
        // given
        when(orderDao.findById(주문_1.getId()))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(주문_1.getId(), 주문_2);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문상태를 변경할 주문이 이미 계산완료된 상태라면 예외가 발생한다")
    @Test
    void change_order_exception_test2() {
        // given
        주문_1.setOrderStatus(OrderStatus.COMPLETION.name());
        when(orderDao.findById(주문_1.getId()))
            .thenReturn(Optional.of(주문_1));

        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(주문_1.getId(), 주문_2);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문목록을 조회한다")
    @Test
    void findAll_test() {
        // given
        when(orderDao.findAll())
            .thenReturn(Arrays.asList(주문_1, 주문_2));
        when(orderLineItemDao.findAllByOrderId(주문_1.getId()))
            .thenReturn(Collections.emptyList());
        when(orderLineItemDao.findAllByOrderId(주문_2.getId()))
            .thenReturn(Collections.emptyList());

        // when
        List<Order> result = orderService.list();

        // then
        assertThat(result).hasSize(2);
    }

    @DisplayName("주문목록을 조회한다")
    @Test
    void findAll_test_copy() {
        // given
        when(orderRepository.findAllOrderAndItems())
            .thenReturn(Arrays.asList(주문));

        // when
        List<OrderResponse> result = orderService.listCopy();

        // then
        assertThat(result).hasSize(1);
    }
}
