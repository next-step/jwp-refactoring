package kitchenpos.order.application;

import static kitchenpos.order.sample.OrderSample.완료된_후라이트치킨세트_두개_주문;
import static kitchenpos.order.sample.OrderSample.조리중인_후라이트치킨세트_두개_주문;
import static kitchenpos.product.sample.MenuSample.이십원_후라이드치킨_두마리세트;
import static kitchenpos.table.sample.OrderTableSample.빈_세명_테이블;
import static kitchenpos.table.sample.OrderTableSample.채워진_다섯명_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.common.domain.Quantity;
import kitchenpos.common.exception.InvalidStatusException;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.order.ui.request.OrderStatusRequest;
import kitchenpos.order.ui.response.OrderResponse;
import kitchenpos.product.domain.Menu;
import kitchenpos.product.domain.MenuRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
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
    private MenuRepository menuRepository;
    @Mock
    private OrderTableRepository tableRepository;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문을 등록할 수 있다.")
    void create() {
        //given
        long orderTableId = 1L;
        long menuId = 1L;
        long quantity = 2L;
        List<OrderLineItemRequest> orderLineItems =
            Collections.singletonList(new OrderLineItemRequest(menuId, quantity));
        OrderRequest orderRequest = new OrderRequest(orderTableId, orderLineItems);

        Menu 이십원_후라이드치킨_두마리세트 = 이십원_후라이드치킨_두마리세트();
        when(menuRepository.menu(anyLong())).thenReturn(이십원_후라이드치킨_두마리세트);

        OrderTable 채워진_다섯명_테이블 = 채워진_다섯명_테이블();
        when(tableRepository.table(orderTableId)).thenReturn(채워진_다섯명_테이블);

        Order order = 조리중인_후라이트치킨세트_두개_주문();
        when(orderRepository.save(any())).thenReturn(order);

        //when
        orderService.create(orderRequest);

        //then
        requestedOrderSave(orderTableId, menuId, quantity);
    }

    @Test
    @DisplayName("등록하려는 주문의 테이블 정보는 반드시 존재해야 한다.")
    void create_notExistsOrderTable_thrownException() {
        //given
        long orderTableId = 1L;
        OrderRequest orderRequest = new OrderRequest(orderTableId,
            Collections.singletonList(new OrderLineItemRequest(1L, 2)));
        when(tableRepository.table(orderTableId)).thenThrow(new NotFoundException("no table"));

        //when
        ThrowingCallable createCallable = () -> orderService.create(orderRequest);

        //then
        assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(createCallable);
    }

    @Test
    @DisplayName("등록하려는 주문의 모든 메뉴는 등록되어 있어야 한다.")
    void create_notExistsMenu_thrownException() {
        //given
        long orderTableId = 1L;
        OrderRequest orderRequest = new OrderRequest(orderTableId,
            Collections.singletonList(new OrderLineItemRequest(1L, 2)));

        OrderTable 채워진_다섯명_테이블 = 채워진_다섯명_테이블();
        when(tableRepository.table(orderTableId)).thenReturn(채워진_다섯명_테이블);
        when(menuRepository.menu(anyLong())).thenThrow(new NotFoundException("no menu"));

        //when
        ThrowingCallable createCallable = () -> orderService.create(orderRequest);

        //then
        assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(createCallable);
    }

    @Test
    @DisplayName("등록하려는 주문의 상품 항목이 비어있으면 안된다.")
    void create_emptyOrderLineItems_thrownException() {
        //given
        long orderTableId = 1L;
        OrderRequest orderRequest = new OrderRequest(orderTableId, Collections.emptyList());

        OrderTable 채워진_다섯명_테이블 = 채워진_다섯명_테이블();
        when(tableRepository.table(orderTableId)).thenReturn(채워진_다섯명_테이블);

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

        Menu 이십원_후라이드치킨_두마리세트 = 이십원_후라이드치킨_두마리세트();
        when(menuRepository.menu(anyLong())).thenReturn(이십원_후라이드치킨_두마리세트);

        OrderTable 빈_세명_테이블 = 빈_세명_테이블();
        when(tableRepository.table(anyLong())).thenReturn(빈_세명_테이블);

        //when
        ThrowingCallable createCallable = () -> orderService.create(orderRequest);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(createCallable);
    }

    @Test
    @DisplayName("주문들을 조회할 수 있다.")
    void list() {
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

        Order 조리중인_후라이트치킨세트_두개_주문 = 조리중인_후라이트치킨세트_두개_주문();
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(조리중인_후라이트치킨세트_두개_주문));

        //when
        OrderResponse response = orderService.changeOrderStatus(1L, orderRequest);

        //then
        assertThat(response)
            .extracting(OrderResponse::getOrderStatus)
            .isEqualTo(updatedStatus.name());
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
        assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(changeCallable)
            .withMessageEndingWith("찾을 수 없습니다.");
    }

    @Test
    @DisplayName("완료된 주문의 상태를 변경할 수 없다.")
    void changeOrderStatus_completedOrder_thrownException() {
        //given
        OrderStatusRequest updateRequest = new OrderStatusRequest(OrderStatus.MEAL.name());

        Order 완료된_후라이트치킨세트_두개_주문 = 완료된_후라이트치킨세트_두개_주문();
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(완료된_후라이트치킨세트_두개_주문));

        //when
        ThrowingCallable changeCallable = () -> orderService.changeOrderStatus(1L, updateRequest);

        //then
        assertThatExceptionOfType(InvalidStatusException.class)
            .isThrownBy(changeCallable);
    }

    private void requestedOrderSave(long orderTableId, long expectedMenuId, long expectedQuantity) {
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, only()).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();
        assertAll(
            () -> assertThat(savedOrder)
                .extracting(Order::status, order -> order.table().id())
                .containsExactly(OrderStatus.COOKING, orderTableId),
            () -> assertThat(savedOrder.lineItems()).first()
                .extracting(OrderLineItem::quantity, order -> order.menu().id())
                .containsExactly(Quantity.from(expectedQuantity), expectedMenuId)
        );
    }
}
