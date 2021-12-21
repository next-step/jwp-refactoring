package kitchenpos.table.application;

import kitchenpos.common.exception.InvalidOrderTableException;
import kitchenpos.common.exception.InvalidTableGroupSizeException;
import kitchenpos.common.exception.NotFoundOrderTableException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.table.domain.OrderTableTest.*;
import static kitchenpos.table.domain.TableGroupTest.테이블그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문 테이블 그룹 테스트")
public class TableGroupServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private TableGroup tableGroup;

    @Test
    @DisplayName("테이블 그룹 생성")
    void createTest() {
        // given
        TableGroupRequest 요청_데이터 = new TableGroupRequest(Arrays.asList(1L, 2L));
        List<OrderTable> orderTables = Arrays.asList(빈자리, 빈자리);
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(orderTables);
        given(tableGroupRepository.save(any())).willReturn(tableGroup);
        // when
        TableGroupResponse actual = tableGroupService.create(요청_데이터);
        // then
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    @DisplayName("주문 테이블은 2개 이상이여야 한다.")
    void orderTableCountExceptionTest() {
        // given
        TableGroupRequest 요청_데이터 = new TableGroupRequest(Collections.singletonList(1L));
        List<OrderTable> orderTables = Collections.singletonList(빈자리);
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(orderTables);
        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(요청_데이터))
                .isInstanceOf(InvalidTableGroupSizeException.class);
    }

    @Test
    @DisplayName("등록이 안된 주문 테이블에서는 주문 할 수 없다.")
    void notFoundOrderTablesTest() {
        // given
        TableGroupRequest 요청_데이터 = new TableGroupRequest(Arrays.asList(1L, 2L, 3L));
        List<OrderTable> orderTables = Arrays.asList(빈자리, 빈자리);
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(orderTables);
        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(요청_데이터))
                .isInstanceOf(NotFoundOrderTableException.class);
    }

    @Test
    @DisplayName("테이블 그룹이 지정된 테이블은 주문 테이블을 생성할 수 없다.")
    void notCreateAlreadyTableGroupTest() {
        // given
        TableGroupRequest 요청_데이터 = new TableGroupRequest(Arrays.asList(1L, 2L));
        OrderTable orderTable1 = new OrderTable(1L, 테이블그룹, 2, true);
        OrderTable orderTable2 = new OrderTable(2L, 테이블그룹, 2, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(orderTables);
        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(요청_데이터))
                .isInstanceOf(InvalidOrderTableException.class);
    }

    @Test
    @DisplayName("빈 테이블이 아니면 주문 테이블을 생성 할 수 없다.")
    void notCreateNonEmptyTest() {
        // given
        TableGroupRequest 요청_데이터 = new TableGroupRequest(Arrays.asList(1L, 2L));
        OrderTable orderTable1 = new OrderTable(1L, null, 2, false);
        OrderTable orderTable2 = new OrderTable(2L, null, 2, false);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(orderTables);
        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(요청_데이터))
                .isInstanceOf(InvalidOrderTableException.class);
    }

    @Test
    @DisplayName("테이블 그룹 해제")
    void ungroupTestTest() {
        // given
        OrderTable orderTable = mock(OrderTable.class);
        given(orderTableRepository.findAllByTableGroupId(anyLong()))
                .willReturn(Collections.singletonList(orderTable));
        given(orderRepository.existsByOrderTableInAndOrderStatusIn(anyList(), anyList()))
                .willReturn(false);
        given(orderTableRepository.save(any())).willReturn(orderTable);
        // when
        tableGroupService.ungroup(1L);
        // then
        verify(orderTableRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("주문 상태가 `조리(COOKING)`, `식사(MEAL)`상태이면 해제 할 수 없다.")
    void notUngroupOrderStatusTest() {
        // given
        OrderTable orderTable = mock(OrderTable.class);
        given(orderTableRepository.findAllByTableGroupId(anyLong()))
                .willReturn(Collections.singletonList(orderTable));
        given(orderRepository.existsByOrderTableInAndOrderStatusIn(anyList(), anyList()))
                .willReturn(true);
        // when
        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
