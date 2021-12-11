package kitchenpos.application;

import static kitchenpos.application.sample.OrderLineItemSample.후라이드치킨세트_두개;
import static kitchenpos.application.sample.OrderSample.완료된_후라이트치킨세트_두개_주문;
import static kitchenpos.application.sample.OrderSample.조리중인_후라이트치킨세트_두개_주문;
import static kitchenpos.application.sample.OrderTableSample.빈_세명_테이블;
import static kitchenpos.application.sample.OrderTableSample.채워진_다섯명_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.order.ui.request.OrderStatusRequest;
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
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
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
        OrderRequest orderRequest = new OrderRequest(orderTableId,
            Collections.singletonList(new OrderLineItemRequest(menuId, 2)));

        when(menuDao.countByIdIn(anyList())).thenReturn(1L);
        when(orderTableDao.findById(orderTableId))
            .thenReturn(Optional.of(채워진_다섯명_테이블()));

        Order order = 조리중인_후라이트치킨세트_두개_주문();
        when(orderDao.save(any())).thenReturn(order);
        OrderLineItem orderLineItem = 후라이드치킨세트_두개();
        orderLineItem.setOrderId(order.getId());
        when(orderLineItemDao.save(any())).thenReturn(orderLineItem);

        //when
        orderService.create(orderRequest);

        //then
        assertAll(
            () -> requestedOrderSave(orderTableId),
            () -> requestedOrderLineItemSave(menuId, order)
        );
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
        when(orderDao.findAll()).thenReturn(Collections.singletonList(cookingOrder));

        //when
        orderService.list();

        //then
        verify(orderDao, only()).findAll();
        verify(orderLineItemDao, only()).findAllByOrderId(cookingOrder.getId());
    }

    @Test
    @DisplayName("주문 상태를 변경할 수 있다.")
    void changeOrderStatus() {
        //given
        OrderStatus updatedStatus = OrderStatus.MEAL;
        OrderStatusRequest orderRequest = new OrderStatusRequest(updatedStatus.name());

        Order cookingOrder = 조리중인_후라이트치킨세트_두개_주문();
        when(orderDao.findById(anyLong())).thenReturn(Optional.of(cookingOrder));

        //when
        orderService.changeOrderStatus(1L, orderRequest);

        //then
        requestedOrderUpdate(updatedStatus);
    }

    @Test
    @DisplayName("변경 요청한 주문 id 는 저장되어 있어야 한다.")
    void changeOrderStatus_notExistOrder_thrownException() {
        //given
        OrderStatusRequest updateRequest = new OrderStatusRequest(OrderStatus.MEAL.name());
        when(orderDao.findById(anyLong())).thenReturn(Optional.empty());

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
        when(orderDao.findById(anyLong())).thenReturn(Optional.of(완료된_후라이트치킨세트_두개_주문()));

        //when
        ThrowingCallable changeCallable = () -> orderService.changeOrderStatus(1L, updateRequest);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(changeCallable);
    }

    private void requestedOrderLineItemSave(long menuId, Order expectedOrder) {
        ArgumentCaptor<OrderLineItem> orderLineItemCaptor = ArgumentCaptor
            .forClass(OrderLineItem.class);
        verify(orderLineItemDao, only()).save(orderLineItemCaptor.capture());
        assertThat(orderLineItemCaptor.getValue())
            .extracting(OrderLineItem::getMenuId, OrderLineItem::getOrderId)
            .containsExactly(menuId, expectedOrder.getId());
    }

    private void requestedOrderSave(long orderTableId) {
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderDao, only()).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();
        assertAll(
            () -> assertThat(savedOrder)
                .extracting(Order::getOrderStatus, Order::getOrderTableId)
                .containsExactly(OrderStatus.COOKING.name(), orderTableId),
            () -> assertThat(savedOrder.getOrderedTime())
                .isEqualToIgnoringMinutes(LocalDateTime.now())
        );
    }

    private void requestedOrderUpdate(OrderStatus updatedStatus) {
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderDao, times(1)).save(orderCaptor.capture());
        assertAll(
            () -> assertThat(orderCaptor.getValue())
                .extracting(Order::getOrderStatus)
                .isEqualTo(updatedStatus.name())
        );
    }

}
