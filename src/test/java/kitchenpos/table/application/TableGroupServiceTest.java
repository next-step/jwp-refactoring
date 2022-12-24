package kitchenpos.table.application;

import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.fixture.OrderTableFixture;
import kitchenpos.table.dto.CreateTableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import static kitchenpos.table.application.TableGroupService.ORDER_STATUS_EXCEPTION_MESSAGE;
import static kitchenpos.table.domain.OrderTables.ORDER_TABLE_MINIMUM_SIZE_EXCEPTION_MESSAGE;
import static kitchenpos.table.domain.fixture.TableGroupFixture.tableGroupA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("TableGroupService")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void create() {

        given(orderTableRepository.findAllById(any())).willReturn(Arrays.asList(OrderTableFixture.orderTableA(null, true), OrderTableFixture.orderTableA(null, true)));
        given(tableGroupRepository.save(any())).willReturn(tableGroupA());

        TableGroupResponse saveTableGroup = tableGroupService.create(new CreateTableGroupRequest(Arrays.asList(1L, 2L)));
        assertThat(saveTableGroup.getCreatedDate()).isNotNull();
    }

    @DisplayName("테이블 그룹을 생성한다. / 주문 테이블의 갯수가 2보다 작을 수 없다.")
    @Test
    void create_fail_minimumSize() {

        given(orderTableRepository.findAllById(any())).willReturn(Collections.singletonList(OrderTableFixture.orderTableA(null, true)));

        assertThatThrownBy(() -> {
            tableGroupService.create(new CreateTableGroupRequest(Collections.singletonList(1L)));
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_TABLE_MINIMUM_SIZE_EXCEPTION_MESSAGE);
    }
//
//    @DisplayName("테이블 그룹을 해제한다.")
//    @Test
//    void unGroup_success() {
//
//        tableGroupService.ungroup(1L);
//
//        테이블_그룹_해제_검증됨(tableGroup);
//    }

    @DisplayName("테이블 그룹을 해제한다. / 요리중일 경우 해제할 수 없다.")
    @Test
    void unGroup_fail_cooking() {

        given(tableGroupRepository.findById(any())).willReturn(Optional.of(tableGroupA()));

//        doThrow(new IllegalArgumentException(ORDER_STATUS_NOT_COMPLETION_EXCEPTION_MESSAGE))
//                .when(tableGroupOrderValidator).validateComplete(any());

        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_STATUS_EXCEPTION_MESSAGE);
    }

    @DisplayName("테이블 그룹을 해제한다. / 식사중일 경우 해제할 수 없다.")
    @Test
    void unGroup_fail_meal() {

        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_STATUS_EXCEPTION_MESSAGE);
    }

    private void 테이블_그룹_해제_검증됨(TableGroup tableGroup) {
        for (OrderTable orderTable : tableGroup.getOrderTables()) {
            OrderTable find = orderTableRepository.findById(orderTable.getId()).orElseThrow(NoSuchElementException::new);
            assertThat(find.getTableGroup()).isNull();
        }
    }
}
