package kitchenpos.order.application;

import static kitchenpos.order.application.sample.OrderSample.완료된_후라이트치킨세트_두개_주문;
import static kitchenpos.order.application.sample.OrderSample.조리중인_후라이트치킨세트_두개_주문;
import static kitchenpos.table.application.sample.OrderTableSample.빈_세명_테이블;
import static kitchenpos.table.application.sample.OrderTableSample.채워진_다섯명_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.order.ui.request.OrderStatusRequest;
import kitchenpos.table.domain.OrderTableDao;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 서비스")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuDao menuDao;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문을 등록할 수 있다.")
    void create() {
        //given
        long orderTableId = 1L;
        long menuId = 1L;
        long quantity = 2L;
        List<OrderLineItemRequest> orderLineItems = Collections
            .singletonList(new OrderLineItemRequest(menuId, quantity));
        OrderRequest orderRequest = new OrderRequest(orderTableId,
            orderLineItems);

        when(menuDao.countByIdIn(anyList())).thenReturn(1L);
        when(orderTableDao.findById(orderTableId))
            .thenReturn(Optional.of(채워진_다섯명_테이블()));

        Order order = 조리중인_후라이트치킨세트_두개_주문();
        when(orderRepository.save(any())).thenReturn(order);

        //when
        orderService.create(orderRequest);

        //then
        requestedOrderSave(orderTableId, menuId, quantity);
    }

    @Test
    @DisplayName("등록하려는 주문의 상품 항목이 비어있으면 안된다.")
    void create_emptyOrderLineItems_thrownException() {
        //given
        OrderRequest orderRequest = new OrderRequest(1L, Collections.emptyList());

        //when
        ThrowingCallable createCallable = () -> orderService.create(orderRequest);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(createCallable);
    }

    @Test
    @DisplayName("등록하려는 주문의 모든 메뉴는 등록되어 있어야 한다.")
    void create_notExistsMenu_thrownException() {
        //given
        OrderRequest orderRequest = new OrderRequest(1L,
            Collections.singletonList(new OrderLineItemRequest(1L, 2)));
        when(menuDao.countByIdIn(anyList())).thenReturn(0L);

        //when
        ThrowingCallable createCallable = () -> orderService.create(orderRequest);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(createCallable);
    }

    @Test
    @DisplayName("등록하려는 주문의 테이블 정보는 반드시 존재해야 한다.")
    void create_notExistsOrderTable_thrownException() {
        //given
        OrderRequest orderRequest = new OrderRequest(1L,
            Collections.singletonList(new OrderLineItemRequest(1L, 2)));
        when(menuDao.countByIdIn(anyList())).thenReturn(1L);
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.empty());

        //when
        ThrowingCallable createCallable = () -> orderService.create(orderRequest);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(createCallable);
    }

    @Test
    @DisplayName("등록하려는 주문의 테이블은 비어있지 않아야 한다.")
    void create_emptyOrderTable_thrownException() {
        //given
        OrderRequest orderRequest = new OrderRequest(1L,
            Collections.singletonList(new OrderLineItemRequest(1L, 2)));
        when(menuDao.countByIdIn(anyList())).thenReturn(1L);
        when(orderTableDao.findById(anyLong()))
            .thenReturn(Optional.of(빈_세명_테이블()));

        //when
        ThrowingCallable createCallable = () -> orderService.create(orderRequest);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(createCallable);
    }

    @Test
    @DisplayName("주문들을 조회할 수 있다.")
    void list() {
        //given
        Order cookingOrder = 조리중인_후라이트치킨세트_두개_주문();
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(cookingOrder));

        //when
        orderService.list();

        //then
        verify(orderRepository, only()).findAll();
    }

    @Test
    @DisplayName("주문 상태를 변경할 수 있다.")
    void changeOrderStatus() {
        //given
        OrderStatus updatedStatus = OrderStatus.MEAL;
        OrderStatusRequest orderRequest = new OrderStatusRequest(updatedStatus.name());

        Order mockOrder = spy(조리중인_후라이트치킨세트_두개_주문());
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(mockOrder));

        //when
        orderService.changeOrderStatus(1L, orderRequest);

        //then
        verify(mockOrder, times(1)).setOrderStatus(updatedStatus);
    }

    @Test
    @DisplayName("변경 요청한 주문 id 는 저장되어 있어야 한다.")
    void changeOrderStatus_notExistOrder_thrownException() {
        //given
        OrderStatusRequest updateRequest = new OrderStatusRequest(OrderStatus.MEAL.name());
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        ThrowingCallable changeCallable = () -> orderService.changeOrderStatus(1L, updateRequest);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(changeCallable);
    }

    @Test
    @DisplayName("완료된 주문의 상태를 변경할 수 없다.")
    void changeOrderStatus_completedOrder_thrownException() {
        //given
        OrderStatusRequest updateRequest = new OrderStatusRequest(OrderStatus.MEAL.name());
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(완료된_후라이트치킨세트_두개_주문()));

        //when
        ThrowingCallable changeCallable = () -> orderService.changeOrderStatus(1L, updateRequest);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(changeCallable);
    }

    private void requestedOrderSave(long orderTableId, long expectedMenuId, long expectedQuantity) {
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, only()).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();
        assertAll(
            () -> assertThat(savedOrder)
                .extracting(Order::getOrderStatus, Order::getOrderTableId)
                .containsExactly(OrderStatus.COOKING.name(), orderTableId),
            () -> assertThat(savedOrder.getOrderLineItems().list()).first()
                .extracting(OrderLineItem::getMenuId, OrderLineItem::getQuantity)
                .containsExactly(expectedMenuId, Quantity.from(expectedQuantity))
        );
    }
}
