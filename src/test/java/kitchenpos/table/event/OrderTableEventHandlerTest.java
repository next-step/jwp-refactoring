package kitchenpos.table.event;

import kitchenpos.exception.OrderTableException;
import kitchenpos.exception.TableGroupException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.enums.OrderStatus;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("주문 테이블 이벤트 핸들러 테스트")
@ExtendWith(MockitoExtension.class)
class OrderTableEventHandlerTest {

    @Mock
    private TableService tableService;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderTableEventHandler orderTableEventHandler;

    private OrderTable 일번_테이블;
    private OrderTable 이번_테이블;

    @BeforeEach
    void setUp() {
        일번_테이블 = new OrderTable(1L, 0, true);
        이번_테이블 = new OrderTable(2L, 0, true);
    }

    @Test
    void 주문_테이블_그룹핑() {
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());
        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        OrderTableGroupEvent orderTableGroupEvent = new OrderTableGroupEvent(tableGroup, orderTableIds);
        when(tableService.findAllByIds(orderTableIds)).thenReturn(new OrderTables(Arrays.asList(일번_테이블, 이번_테이블)));
        orderTableEventHandler.groupOrderTable(orderTableGroupEvent);
    }

    @Test
    void 주문_테이블이_1개만있을때_테이블그룹_생성_요청_시_에러_발생() {
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());
        tableGroup.addOrderTable(일번_테이블);
        OrderTableGroupEvent orderTableGroupEvent = new OrderTableGroupEvent(tableGroup, Arrays.asList(1L));
        assertThatThrownBy(() -> orderTableEventHandler.groupOrderTable(orderTableGroupEvent)).isInstanceOf(TableGroupException.class);
    }

    @Test
    void 주문_상태가_완료_상태가_아닌_경우_에서_그룹핑_해제_요청_시_에러_발생() {
        Orders 일번_주문 = new Orders(1L, 1L, OrderStatus.COMPLETION, LocalDateTime.now());
        Orders 이번_주문 = new Orders(2L, 2L, OrderStatus.MEAL, LocalDateTime.now());
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());
        tableGroup.addOrderTable(일번_테이블);
        tableGroup.addOrderTable(이번_테이블);
        when(orderRepository.findAllByOrderTableIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(일번_주문, 이번_주문));
        OrderTableUngroupEvent orderTableGroupEvent = new OrderTableUngroupEvent(tableGroup);
        assertThatThrownBy(() -> orderTableEventHandler.ungroupOrderTable(orderTableGroupEvent)).isInstanceOf(TableGroupException.class);
    }

    @Test
    void 테이블_상태_변경_시_그룹핑된_테이블일_경우_에러_발생() {
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());
        일번_테이블.withTableGroup(tableGroup);
        OrderTableChangeEmptyValidEvent orderTableChangeEmptyValidEvent = new OrderTableChangeEmptyValidEvent(일번_테이블);
        assertThatThrownBy(() -> orderTableEventHandler.changeEmptyOrderTable(orderTableChangeEmptyValidEvent)).isInstanceOf(OrderTableException.class);
    }

    @Test
    void 테이블_상태_변경_시_주문_상태가_완료가_아닌_경우_에러_발생() {
        Orders 일번_주문 = new Orders(1L, 1L, OrderStatus.MEAL, LocalDateTime.now());
        when(orderRepository.findByOrOrderTableId(1L)).thenReturn(java.util.Optional.of(일번_주문));
        OrderTableChangeEmptyValidEvent orderTableChangeEmptyValidEvent = new OrderTableChangeEmptyValidEvent(일번_테이블);
        assertThatThrownBy(() -> orderTableEventHandler.changeEmptyOrderTable(orderTableChangeEmptyValidEvent)).isInstanceOf(OrderTableException.class);
    }
}
