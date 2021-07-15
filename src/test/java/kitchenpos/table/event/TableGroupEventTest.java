package kitchenpos.table.event;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.event.TableGroupCreatedEvent;
import kitchenpos.table.event.TableGroupEventHandler;
import kitchenpos.table.event.TableGroupUngroupEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableGroupEventTest {
    private TableGroupCreatedEvent tableGroupCreatedEvent;
    private TableGroupUngroupEvent tableUngroupEvent;
    private TableGroupEventHandler tableGroupEventHandler;
    private TableGroupRequest tableGroupRequest;
    private TableGroup tableGroup;
    private OrderTable orderTable_tableGroup_null_1;
    private OrderTable orderTable_tableGroup_null_2;
    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        그룹미지정된_주문테이블_생성();
        그룹지정된_주문테이블_생성();
        테이블_그룹_관련_이벤트_생성();
    }

    @DisplayName("주문 테이블을 그룹화 한다.")
    @Test
    void create() {
        List<OrderTable> orderTables = Arrays.asList(orderTable_tableGroup_null_1, orderTable_tableGroup_null_2);

        tableGroupRequest = 그룹_지정전_테이블_그룹_요청_생성();

        given(orderTableRepository.findAllById(anyList())).willReturn(orderTables);

        주문_테이블_그룹화_요청();

        주문_테이블_그룹회돰();
    }

    @DisplayName("주문테이블 2개 미만인 경우 그룹화 예외 발생")
    @Test
    void group_exception() {
        tableGroupCreatedEvent = new TableGroupCreatedEvent(tableGroupRequest, tableGroup);

        주문테이블_2개_미만인_경우_그룹화_예외_발생함();
    }

    @DisplayName("등록되지 않은 주문테이블 그룹화 할 경우 예외발생")
    @Test
    void group_exception2() {
        등록되지_않은_주문테이블_요청_이벤트_생성();

        등록되지_않은_주문테이블_그룹화_예외_발생함(tableGroupRequest);
    }

    @DisplayName("그룹으로 묶여있는 주문 테이블 그룹화 할 경우 예외 발생")
    @Test
    void group_exception3() {
        List<OrderTable> orderTables = 그룹으로_묶여있는_주문_테이블_생성();
        tableGroupRequest = 테이블_그룹_요청_생성();

        given(orderTableRepository.findAllById(anyList())).willReturn(orderTables);
        tableGroupCreatedEvent = new TableGroupCreatedEvent(tableGroupRequest, new TableGroup(1L));

        그룹으로_묶인_주문_테이블_그룹화_예외_발생함();

    }

    @DisplayName("주문의 상태가 조리 또는 식사인 경우 그룹을 해제할 수 없다.")
    @Test
    void ungroup_exception() {
        TableGroup tableGroup = new TableGroup(1L);
        OrderTable orderTable1 = new OrderTable(1L, tableGroup, 3, true);

        Order order1 = new Order(1L, orderTable1.getId(), OrderStatus.COOKING);

        given(orderTableRepository.findAllByTableGroupId(anyLong())).willReturn(Arrays.asList(orderTable1));
        given(orderRepository.findByOrderTaleId(anyLong())).willReturn(order1);

        주문상태_완료가_아닐경우_그룹해제_예외_발생함();
    }

    @DisplayName("그룹을 해제한다.")
    @Test
    void ungroup() {
        TableGroup tableGroup = new TableGroup(1L);
        OrderTable orderTable1 = new OrderTable(1L, tableGroup, 3, true);
        Order order1 = new Order(1L, orderTable1.getId(), OrderStatus.COMPLETION);

        given(orderTableRepository.findAllByTableGroupId(anyLong())).willReturn(Arrays.asList(orderTable1));
        given(orderRepository.findByOrderTaleId(anyLong())).willReturn(order1);

        그룹_해제_요청();

        그룹_해제됨();
    }

    private TableGroupRequest 그룹_지정전_테이블_그룹_요청_생성() {
        OrderTableRequest orderTableRequest1 = new OrderTableRequest(1L,  1, false);
        OrderTableRequest orderTableRequest2 = new OrderTableRequest(2L,  1,false);
        return new TableGroupRequest(1L, Arrays.asList(orderTableRequest1, orderTableRequest2));
    }

    private void 등록되지_않은_주문테이블_요청_이벤트_생성() {
        OrderTableRequest orderTableRequest1 = new OrderTableRequest(1L, 1L, 1, false);
        OrderTableRequest orderTableRequest2 = new OrderTableRequest(2L, 1L, 1,false);
        tableGroupRequest = new TableGroupRequest(1L, Arrays.asList(orderTableRequest1, orderTableRequest2));
        tableGroupCreatedEvent = new TableGroupCreatedEvent(tableGroupRequest, tableGroup);

    }

    private void 등록되지_않은_주문테이블_그룹화_예외_발생함(TableGroupRequest tableGroupRequest) {
        assertThatThrownBy(() -> tableGroupEventHandler.savedOrderTables(tableGroupCreatedEvent))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록된 주문테이블만 그룹화 할 수 있습니다.");
    }

    private void 주문테이블_2개_미만인_경우_그룹화_예외_발생함() {
        assertThatThrownBy(() -> tableGroupEventHandler.savedOrderTables(tableGroupCreatedEvent))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("그룹테이블은 2개 이상이어야 그룹화가 가능합니다.");
    }

    private void 주문_테이블_그룹회돰() {
        assertThat(orderTable_tableGroup_null_1.getTableGroupId()).isEqualTo(tableGroup.getId());
        assertThat(orderTable_tableGroup_null_2.getTableGroupId()).isEqualTo(tableGroup.getId());
    }

    private void 주문_테이블_그룹화_요청() {
        tableGroupCreatedEvent = new TableGroupCreatedEvent(tableGroupRequest, tableGroup);
        tableGroupEventHandler.savedOrderTables(tableGroupCreatedEvent);
    }

    private TableGroupRequest 테이블_그룹_요청_생성() {
        OrderTableRequest orderTableRequest1 = new OrderTableRequest(1L, 1L, 1, false);
        OrderTableRequest orderTableRequest2 = new OrderTableRequest(2L, 1L, 1,false);
        return new TableGroupRequest(1L, Arrays.asList(orderTableRequest1, orderTableRequest2));
    }

    private List<OrderTable> 그룹으로_묶여있는_주문_테이블_생성() {
        OrderTable orderTable1 = new OrderTable(1L, tableGroup, 3, true);
        OrderTable orderTable2 = new OrderTable(1L, tableGroup, 3, true);
        return Arrays.asList(orderTable1, orderTable2);
    }

    private void 그룹으로_묶인_주문_테이블_그룹화_예외_발생함() {
        assertThatThrownBy(() -> tableGroupEventHandler.savedOrderTables(tableGroupCreatedEvent))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 그룹으로 묶여있는 경우 그룹화 할 수 없습니다.");
    }

    private void 주문상태_완료가_아닐경우_그룹해제_예외_발생함() {
        assertThatThrownBy(() -> tableGroupEventHandler.ungroupOrderTables(tableUngroupEvent))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블의 주문 상태가 조리 또는 식사인 경우 테이블을 비울 수 없습니다.");
    }

    private void 그룹_해제_요청() {
        tableGroupEventHandler.ungroupOrderTables(tableUngroupEvent);
    }

    private void 그룹_해제됨() {
        assertThat(orderTable1.getTableGroupId()).isEqualTo(-1);
    }

    private void 그룹지정된_주문테이블_생성() {
        orderTable1 = new OrderTable(1L, tableGroup, 3, true);
        orderTable2 = new OrderTable(1L, tableGroup, 3, true);
    }

    private void 그룹미지정된_주문테이블_생성() {
        orderTable_tableGroup_null_1 = new OrderTable(1L, null, 3, true);
        orderTable_tableGroup_null_2 = new OrderTable(2L, null, 3, true);

    }

    private void 테이블_그룹_관련_이벤트_생성() {
        tableGroup = new TableGroup(1L);
        tableGroupRequest = new TableGroupRequest();
        tableGroupCreatedEvent = new TableGroupCreatedEvent(tableGroupRequest, tableGroup);
        tableGroupEventHandler = new TableGroupEventHandler(orderTableRepository, orderRepository);
        tableUngroupEvent = new TableGroupUngroupEvent(1L);
    }
}
