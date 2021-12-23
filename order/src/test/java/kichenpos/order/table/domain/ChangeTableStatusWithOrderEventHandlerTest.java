package kichenpos.order.table.domain;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kichenpos.order.order.domain.event.OrderCreatedEvent;
import kichenpos.order.order.domain.event.OrderStatusChangedEvent;
import kichenpos.order.table.infrastructure.OrderTableClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 이벤트에 따른 테이블 상태 변경")
@ExtendWith(MockitoExtension.class)
class ChangeTableStatusWithOrderEventHandlerTest {

    @Mock
    private OrderTableClient tableClient;

    @InjectMocks
    private ChangeTableStatusWithOrderEventHandler eventHandler;

    @Test
    @DisplayName("주문이 생성되면 테이블은 주문 상태로 변경 요청됨")
    void orderTable() {
        //given
        long tableId = 1L;
        OrderCreatedEvent event = mock(OrderCreatedEvent.class);
        when(event.tableId()).thenReturn(tableId);

        //when
        eventHandler.orderTable(event);

        //then
        verify(tableClient, only()).changeOrdered(tableId);
    }

    @Test
    @DisplayName("주문이 완료되면 테이블은 종료 상태로 변경 요청됨")
    void finishTable() {
        //given
        long tableId = 1L;
        OrderStatusChangedEvent event = mock(OrderStatusChangedEvent.class);
        when(event.tableId()).thenReturn(tableId);
        when(event.isCompleted()).thenReturn(true);

        //when
        eventHandler.finishTable(event);

        //then
        verify(tableClient, only()).changeFinish(tableId);
    }

    @Test
    @DisplayName("주문이 완료가 아닌 상태로 변경되면 테이블 상태 변경 요청되지 않음")
    void finishTable_notCompleted() {
        //given
        OrderStatusChangedEvent event = mock(OrderStatusChangedEvent.class);
        when(event.isCompleted()).thenReturn(false);

        //when
        eventHandler.finishTable(event);

        //then
        verify(tableClient, never()).changeFinish(anyLong());
    }
}
