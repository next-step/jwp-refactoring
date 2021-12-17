package kitchenpos.order.domain;

import static kitchenpos.order.sample.OrderLineItemSample.이십원_후라이트치킨_두마리세트_한개_주문_항목;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import kitchenpos.common.exception.NotFoundException;
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

@DisplayName("주문 생성 서비스")
@ExtendWith(MockitoExtension.class)
class OrderCreateServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderValidator validator;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderCreateService createService;

    @Test
    @DisplayName("주문을 등록할 수 있다.")
    void create() {
        //given
        long orderTableId = 1L;
        OrderLineItem lineItem = 이십원_후라이트치킨_두마리세트_한개_주문_항목();

        Order order = mock(Order.class);
        when(order.tableId()).thenReturn(orderTableId);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderTable table = mock(OrderTable.class);
        when(orderTableRepository.table(orderTableId)).thenReturn(table);

        //when
        createService.create(orderTableId, Collections.singletonList(lineItem));

        //then
        assertAll(
            () -> requestedSaveOrder(orderTableId, lineItem),
            () -> verify(table, only()).ordered()
        );
    }

    @Test
    @DisplayName("등록하려는 주문의 테이블 정보는 반드시 존재해야 한다.")
    void create_notExistsOrderTable_thrownException() {
        //given
        doThrow(NotFoundException.class)
            .when(validator)
            .validate(any(Order.class));

        //when
        ThrowingCallable createCallable = () ->
            createService.create(1L, Collections.singletonList(이십원_후라이트치킨_두마리세트_한개_주문_항목()));

        //then
        assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(createCallable);
    }


    @Test
    @DisplayName("등록하려는 주문의 상품 항목이 비어있으면 안된다.")
    void create_emptyOrderLineItems_thrownException() {
        //given, when
        ThrowingCallable createCallable = () -> createService.create(1L, Collections.emptyList());

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(createCallable);
    }

    @Test
    @DisplayName("등록하려는 주문의 테이블은 비어있지 않아야 한다.")
    void create_emptyOrderTable_thrownException() {
        //given
        doThrow(IllegalArgumentException.class)
            .when(validator)
            .validate(any(Order.class));

        //when
        ThrowingCallable createCallable = () ->
            createService.create(1L, Collections.singletonList(이십원_후라이트치킨_두마리세트_한개_주문_항목()));

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(createCallable);
    }

    private void requestedSaveOrder(long expectedOrderTableId, OrderLineItem expectedLineItem) {
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, only()).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();
        assertAll(
            () -> assertThat(savedOrder.tableId()).isEqualTo(expectedOrderTableId),
            () -> assertThat(savedOrder.lineItems()).first()
                .extracting(OrderLineItem::quantity, OrderLineItem::menu)
                .containsExactly(expectedLineItem.quantity(), expectedLineItem.menu())
        );
    }
}
