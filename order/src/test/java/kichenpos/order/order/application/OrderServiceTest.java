package kichenpos.order.order.application;

import static kichenpos.order.order.sample.OrderSample.조리중인_후라이트치킨세트_두개_주문;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kichenpos.common.domain.Name;
import kichenpos.common.domain.Price;
import kichenpos.common.domain.Quantity;
import kichenpos.common.exception.NotFoundException;
import kichenpos.order.order.domain.Order;
import kichenpos.order.order.domain.OrderCommandService;
import kichenpos.order.order.domain.OrderLineItem;
import kichenpos.order.order.domain.OrderQueryService;
import kichenpos.order.order.domain.OrderStatus;
import kichenpos.order.order.ui.request.OrderLineItemRequest;
import kichenpos.order.order.ui.request.OrderRequest;
import kichenpos.order.order.ui.request.OrderStatusRequest;
import kichenpos.order.product.domain.Menu;
import kichenpos.order.product.domain.MenuQueryService;
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
@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest {

    @Mock
    private OrderCommandService commandService;
    @Mock
    private OrderQueryService queryService;
    @Mock
    private MenuQueryService menuQueryService;

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

        Menu 치킨세트 = Menu.of(menuId, Name.from("치킨세트"), Price.from(BigDecimal.TEN));
        when(menuQueryService.findAllById(anyList()))
            .thenReturn(Collections.singletonList(치킨세트));

        Order 조리중인_후라이트치킨세트_두개_주문 = 조리중인_후라이트치킨세트_두개_주문();
        when(commandService.save(any(Order.class))).thenReturn(조리중인_후라이트치킨세트_두개_주문);

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

        when(menuQueryService.findAllById(anyList())).thenReturn(Collections.emptyList());

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
        OrderRequest orderRequest = new OrderRequest(1L, Collections.emptyList());
        when(menuQueryService.findAllById(anyList())).thenReturn(Collections.emptyList());

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
        verify(queryService, only()).findAll();
    }

    @Test
    @DisplayName("주문 상태를 변경할 수 있다.")
    void changeOrderStatus() {
        //given
        OrderStatus updatedStatus = OrderStatus.MEAL;
        OrderStatusRequest orderRequest = new OrderStatusRequest(updatedStatus.name());

        Order 조리중인_후라이트치킨세트_두개_주문 = 조리중인_후라이트치킨세트_두개_주문();
        when(commandService.changeStatus(anyLong(), any(OrderStatus.class)))
            .thenReturn(조리중인_후라이트치킨세트_두개_주문);

        //when
        orderService.changeOrderStatus(1L, orderRequest);

        //then
        verify(commandService, only()).changeStatus(1L, updatedStatus);
    }

    private void requestedOrderSave(long orderTableId, long expectedMenuId, long expectedQuantity) {
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(commandService, only()).save(orderCaptor.capture());
        Order actualOrder = orderCaptor.getValue();
        assertAll(
            () -> assertThat(actualOrder.tableId()).isEqualTo(orderTableId),
            () -> assertThat(actualOrder.lineItems())
                .first(InstanceOfAssertFactories.type(OrderLineItem.class))
                .extracting(OrderLineItem::quantity, lineItem -> lineItem.menu().id())
                .containsExactly(Quantity.from(expectedQuantity), expectedMenuId)
        );
    }
}
