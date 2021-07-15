package kitchenpos.table.application;

import kitchenpos.exception.DuplicateOrderTableException;
import kitchenpos.exception.FailedUngroupException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableDependencyHelper;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private TableDependencyHelper tableDependencyHelper;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정을 등록할 수 있다.")
    @Test
    void createTest() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, 1, false);
        OrderTable orderTable2 = new OrderTable(2L, 1, false);
        TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable1, orderTable2));

        OrderTable orderTable3 = new OrderTable(3L, 1, false);
        OrderTable orderTable4 = new OrderTable(4L, 1, false);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(3L, 4L));

        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable3, orderTable4));
        given(tableGroupRepository.save(any())).willReturn(tableGroup);

        // when
        TableGroupResponse createdTableGroup = tableGroupService.create(tableGroupRequest);

        // then
        Assertions.assertThat(createdTableGroup.getOrderTables()).hasSize(tableGroup.getOrderTables().size());
    }

    @DisplayName("단체 지정의 주문 테이블 목록이 올바르지 않으면 등록할 수 없다 : 주문 테이블 목록은 등록되어있고 중복되지않은 주문 테이블 이어야 한다.")
    @Test
    void createTest_wrongOrderTable() {
        // given
        OrderTable orderTable = new OrderTable(1L, 1, false);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(1L, 1L));

        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(DuplicateOrderTableException.class);
    }

    @DisplayName("단체 지정된 주문 테이블 목록의 주문의 상태가 완료이어야 한다.")
    @Test
    void ungroupTest_wrongStatus() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, 1, false);
        OrderTable orderTable2 = new OrderTable(2L, 1, false);
        TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable1, orderTable2));

        given(tableGroupRepository.findById(any())).willReturn(Optional.of(tableGroup));
        given(tableDependencyHelper.existsByOrderTableIdInAndOrderStatusNotCompletion(any())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(FailedUngroupException.class);
    }
}
