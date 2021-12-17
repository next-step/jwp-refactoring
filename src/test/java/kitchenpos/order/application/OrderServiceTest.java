package kitchenpos.order.application;

import static kitchenpos.order.sample.OrderSample.조리중인_후라이트치킨세트_두개_주문;
import static kitchenpos.product.sample.MenuSample.이십원_후라이드치킨_두마리세트;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.common.domain.Quantity;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderCreateService;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderStatusChangeService;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.order.ui.request.OrderStatusRequest;
import kitchenpos.product.domain.Menu;
import kitchenpos.product.domain.MenuRepository;
import org.assertj.core.api.InstanceOfAssertFactories;
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
    private OrderCreateService createService;
    @Mock
    private OrderStatusChangeService statusChangeService;
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
        when(menuRepository.findAllById(anyIterable()))
            .thenReturn(Collections.singletonList(이십원_후라이드치킨_두마리세트));

        Order order = 조리중인_후라이트치킨세트_두개_주문();
        when(createService.create(anyLong(), anyList())).thenReturn(order);

        //when
        orderService.create(orderRequest);

        //then
        requestedOrderSave(orderTableId, menuId, quantity);
    }

    @Test
    @DisplayName("등록하려는 주문의 모든 메뉴는 등록되어 있어야 한다.")
    void create_notExistsMenu_thrownException() {
        //given
        long orderTableId = 1L;
        OrderRequest orderRequest = new OrderRequest(orderTableId,
            Collections.singletonList(new OrderLineItemRequest(1L, 2)));

        when(menuRepository.findAllById(anyIterable())).thenReturn(Collections.emptyList());

        //when
        ThrowingCallable createCallable = () -> orderService.create(orderRequest);

        //then
        assertThatExceptionOfType(NotFoundException.class)
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
        orderService.changeOrderStatus(1L, orderRequest);

        //then
        verify(statusChangeService, only()).change(1L, updatedStatus);
    }

    private void requestedOrderSave(long orderTableId, long expectedMenuId, long expectedQuantity) {
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<OrderLineItem>> lineItemsCaptor = ArgumentCaptor.forClass(List.class);
        verify(createService, only()).create(eq(orderTableId), lineItemsCaptor.capture());
        assertAll(
            () -> assertThat(lineItemsCaptor.getValue())
                .first(InstanceOfAssertFactories.type(OrderLineItem.class))
                .extracting(OrderLineItem::quantity, lineItem -> lineItem.menu().id())
                .containsExactly(Quantity.from(expectedQuantity), expectedMenuId)
        );
    }
}
