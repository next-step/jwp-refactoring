package kitchenpos.ordertable.event;

import kitchenpos.order.event.CreateOrderEvent;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.event.handler.CreateOrderEventHandler;
import kitchenpos.ordertable.exception.IllegalOrderTableException;
import kitchenpos.ordertable.exception.NoSuchOrderTableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static kitchenpos.utils.fixture.OrderTableFixtureFactory.createOrderTable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CreateOrderEventHandlerTest {
    @Mock
    private TableService tableService;
    @InjectMocks
    CreateOrderEventHandler createOrderEventHandler;

    private OrderTable 테이블_EMPTY;
    private OrderTable 테이블_NOT_EMPTY;

    @BeforeEach
    void setUp() {
        테이블_EMPTY = createOrderTable(1L, null, 0, true);
        테이블_NOT_EMPTY = createOrderTable(2L, null, 3, false);
    }

    @DisplayName("주문 생성 Event 성공")
    @Test
    void 주문_생성_Event_성공(){
        //given
        CreateOrderEvent event = CreateOrderEvent.from(테이블_NOT_EMPTY.getId());
        given(tableService.findOrderTableById(anyLong())).willReturn(테이블_NOT_EMPTY);

        //then
        assertDoesNotThrow(() -> createOrderEventHandler.handle(event));
    }

    @DisplayName("주문 생성 Event 실패: 테이블이 존재하지 않는다")
    @Test
    void 주문_생성_Event_OrderTable_존재_검증(){
        //given
        CreateOrderEvent event = CreateOrderEvent.from(테이블_EMPTY.getId());
        given(tableService.findOrderTableById(anyLong())).willThrow(NoSuchOrderTableException.class);

        //then
        assertThrows(NoSuchOrderTableException.class, () -> createOrderEventHandler.handle(event));
    }

    @DisplayName("주문 생성 Event 실패: 테이블이 비어있지 않다")
    @Test
    void 주문_생성_Event_OrderTable_Empty_검증(){
        //given
        CreateOrderEvent event = CreateOrderEvent.from(테이블_EMPTY.getId());
        given(tableService.findOrderTableById(anyLong())).willReturn(테이블_EMPTY);

        //then
        assertThrows(IllegalOrderTableException.class, () -> createOrderEventHandler.handle(event));
    }
}
