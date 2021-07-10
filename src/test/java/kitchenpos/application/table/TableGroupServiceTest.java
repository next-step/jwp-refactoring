package kitchenpos.application.table;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.order.OrderTableId;
import kitchenpos.dto.table.TableGroupRequest;
import kitchenpos.event.table.TableGroupCreatedEvent;
import kitchenpos.event.table.TableOrderUngroupEvent;
import kitchenpos.exception.order.InvalidOrderStatusException;
import kitchenpos.exception.order.InvalidOrderTableException;
import kitchenpos.exception.table.NotMatchOrderTableException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private TableService tableService;
    @Mock
    private ApplicationEventPublisher publisher;

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
        given(tableService.getOrderTable(ORDER_TABLE_ID_1L)).willReturn(orderTable1);
        given(tableService.getOrderTable(ORDER_TABLE_ID_2L)).willReturn(orderTable2);
        given(tableService.getAllOrderTablesByIds(any())).willReturn(Lists.list(orderTable1));

        tableGroupRequest = new TableGroupRequest(Lists.list(new OrderTableId(ORDER_TABLE_ID_1L),
                new OrderTableId(ORDER_TABLE_ID_2L)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(NotMatchOrderTableException.class);
    }

    @Test
    @DisplayName("단체 지정 시점에, 주문 테이블이 빈 테이블이 아니라면 단체 지정을 할 수 없다.")
    void exception2_orderTable() {
        given(tableService.getOrderTable(ORDER_TABLE_ID_1L)).willReturn(orderTable1);
        given(tableService.getOrderTable(ORDER_TABLE_ID_2L)).willReturn(orderTable2);
        given(tableService.getAllOrderTablesByIds(any())).willReturn(Lists.list(orderTable1, orderTable2));

        doThrow(InvalidOrderTableException.class).when(publisher).publishEvent(any(TableGroupCreatedEvent.class));

        tableGroupRequest = new TableGroupRequest(Lists.list(new OrderTableId(ORDER_TABLE_ID_1L), new OrderTableId(ORDER_TABLE_ID_2L)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(InvalidOrderTableException.class);
    }

    @Test
    @DisplayName("단체 지정을 취소할 수 있다.")
    void ungroup() {
        List<OrderTable> orderTables = Lists.list(orderTable1, orderTable2);
        given(tableService.getAllOrderTablesByGroupId(ANY_TABLE_GROUP_ID))
                .willReturn(orderTables);

        tableGroupService.ungroup(ANY_TABLE_GROUP_ID);

        verify(publisher).publishEvent(any(TableOrderUngroupEvent.class));
        verify(tableService).makeTableGroupEmpty(orderTable1);
        verify(tableService).makeTableGroupEmpty(orderTable2);
    }

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