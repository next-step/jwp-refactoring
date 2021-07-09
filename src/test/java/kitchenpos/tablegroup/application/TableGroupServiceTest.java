package kitchenpos.tablegroup.application;

import java.util.Optional;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest.OrderTableIdRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

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

    @DisplayName("테이블 그룹 생성 테스트")
    @Test
    void createTest() {
        // given
        OrderTable orderTable1 = new OrderTable(3);
        OrderTable orderTable2 = new OrderTable(4);
        TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable1, orderTable2));

        TableGroupRequest request = new TableGroupRequest(Arrays.asList(new OrderTableIdRequest(1L),
                                                                        new OrderTableIdRequest(2L)));

        Mockito.when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable1));
        Mockito.when(orderTableRepository.findById(2L)).thenReturn(Optional.of(orderTable2));
        Mockito.when(tableGroupRepository.save(any())).thenReturn(tableGroup);

        // when
        TableGroupResponse actual = tableGroupService.create(request);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getOrderTables()).isNotEmpty().hasSize(2);
    }

    @DisplayName("테이블 그룹으로 묶일 주문 테이블이 2개 이하일 경우")
    @Test
    void createTestWrongSize() {
        // given
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(new OrderTableIdRequest(2L)));

        // when
        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("테이블 그룹 해제 테스트")
    @Test
    void ungroupTest() {
        // given
        OrderTable orderTable1 = new OrderTable(3);
        OrderTable orderTable2 = new OrderTable(4);

        Mockito.when(orderTableRepository.findAllByTableGroupId(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));
        Mockito.when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);

        // when
        tableGroupService.ungroup(1L);

        // then
        Mockito.verify(orderTableRepository, Mockito.times(2)).save(any());
    }

    @DisplayName("테이블 그룹으로 묶일 주문 테이블의 상태가 COMPLETION이 아닐 경우")
    @Test
    void ungroupTestWithWrongStatus() {
        // given
        OrderTable orderTable1 = new OrderTable(3);
        OrderTable orderTable2 = new OrderTable(4);

        Mockito.when(orderTableRepository.findAllByTableGroupId(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));
        Mockito.when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);

        // when
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
