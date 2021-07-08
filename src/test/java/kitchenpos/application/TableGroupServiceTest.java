package kitchenpos.application;

import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.order.OrderTableId;
import kitchenpos.dto.order.TableGroupRequest;
import kitchenpos.event.order.TableOrderUngroupEvent;
import kitchenpos.exception.InvalidOrderStatusException;
import kitchenpos.exception.InvalidOrderTableException;
import kitchenpos.exception.NotMatchOrderTableException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderService orderService;
    @Mock
    private OrderTableService orderTableService;
    @Mock
    private ApplicationEventPublisher publisher;

    @Captor
    private ArgumentCaptor<TableOrderUngroupEvent> tableOrderUngroupEventArgumentCaptor;

    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroupRequest tableGroupRequest;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    public final long ORDER_TABLE_ID_1L = 1L;
    public final long ORDER_TABLE_ID_2L = 2L;
    private final long ANY_TABLE_GROUP_ID = 1L;

    @BeforeEach
    void setUp() {

        orderTable1 = OrderTable.of(10, false);
        ReflectionTestUtils.setField(orderTable1, "id", ORDER_TABLE_ID_1L);
        orderTable2 = OrderTable.of(10, false);
        ReflectionTestUtils.setField(orderTable2, "id", ORDER_TABLE_ID_2L);

        tableGroupRequest = new TableGroupRequest(new ArrayList<>());
    }

    @Test
    @DisplayName("단체 지정을 할 경우, 적어도 2개 이상의 주문된 테이블이 존재해야 한다.")
    void exception_create_test() {

        tableGroupRequest = new TableGroupRequest(new ArrayList<>());
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("should have over 2 orderTables");
    }

    @Test
    @DisplayName("단체 지정 시점에, 주문 테이블이 단체 지정시 주문받은 주문 테이블과 숫자가 맞지 않으면 지정할 수 없다.")
    void exception_orderTable() {
        given(orderTableService.getOrderTable(ORDER_TABLE_ID_1L)).willReturn(orderTable1);
        given(orderTableService.getOrderTable(ORDER_TABLE_ID_2L)).willReturn(orderTable2);
        given(orderTableService.getAllOrderTablesByIds(any())).willReturn(Lists.list(orderTable1));

        tableGroupRequest = new TableGroupRequest(Lists.list(new OrderTableId(ORDER_TABLE_ID_1L),
                new OrderTableId(ORDER_TABLE_ID_2L)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(NotMatchOrderTableException.class);
    }

    @Test
    @DisplayName("단체 지정 시점에, 주문 테이블이 빈 테이블이 아니라면 단체 지정을 할 수 없다.")
    void exception2_orderTable() {
        given(orderTableService.getOrderTable(ORDER_TABLE_ID_1L)).willReturn(orderTable1);
        given(orderTableService.getOrderTable(ORDER_TABLE_ID_2L)).willReturn(orderTable2);
        given(orderTableService.getAllOrderTablesByIds(any())).willReturn(Lists.list(orderTable1, orderTable2));

        tableGroupRequest = new TableGroupRequest(Lists.list(new OrderTableId(ORDER_TABLE_ID_1L), new OrderTableId(ORDER_TABLE_ID_2L)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(InvalidOrderTableException.class)
                .hasMessageContaining("savedOrderTable");
    }

    @Test
    @DisplayName("단체 지정을 취소할 수 있다.")
    void ungroup() {
        List<OrderTable> orderTables = Lists.list(orderTable1, orderTable2);
        given(orderTableService.getAllOrderTablesByGroupId(ANY_TABLE_GROUP_ID))
                .willReturn(orderTables);

        tableGroupService.ungroup(ANY_TABLE_GROUP_ID);

        verify(publisher).publishEvent(any(TableOrderUngroupEvent.class));
        verify(orderTableService).makeTableGroupEmpty(orderTable1);
        verify(orderTableService).makeTableGroupEmpty(orderTable2);
    }

//    @Test
//    @DisplayName("단체 지정을 취소할 수 있다.")
//    void ungroup2() {
//        List<OrderTable> orderTables = Lists.list(orderTable1, orderTable2);
//        given(orderTableService.getAllOrderTablesByGroupId(ANY_TABLE_GROUP_ID))
//                .willReturn(orderTables);
//
//        tableGroupService.ungroup(ANY_TABLE_GROUP_ID);
//
//        verify(publisher).publishEvent(tableOrderUngroupEventArgumentCaptor.capture());
//
//        assertThat(tableOrderUngroupEventArgumentCaptor.getValue().getOrderTables()).isEqualTo(orderTables);
//        verify(orderTableService).makeTableGroupEmpty(orderTable1);
//        verify(orderTableService).makeTableGroupEmpty(orderTable2);
//    }

    @Test
    @DisplayName("주문이 식사 또는 조리의 경우 단체 지정을 취소할 수 없다.")
    void exception_upgroup() {

        OrderTable orderTable1 = OrderTable.of(10, false);
        ReflectionTestUtils.setField(orderTable1, "id", 1L);
        OrderTable orderTable2 = OrderTable.of(10, false);
        ReflectionTestUtils.setField(orderTable2, "id", 2L);

        doThrow(InvalidOrderStatusException.class)
                .when(publisher).publishEvent(any(TableOrderUngroupEvent.class));

        assertThatThrownBy(() -> tableGroupService.ungroup(ANY_TABLE_GROUP_ID))
                .isInstanceOf(InvalidOrderStatusException.class);
    }
}