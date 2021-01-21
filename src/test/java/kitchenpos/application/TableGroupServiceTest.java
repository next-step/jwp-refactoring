package kitchenpos.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.OrderTableIdRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("단체 지정 서비스 테스트")
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

    @DisplayName("복수의 주문 테이블을 단체 지정할 수 있다.")
    @Test
    void create1() {
        //given
        ArgumentCaptor<TableGroup> argumentCaptor = ArgumentCaptor.forClass(TableGroup.class);

        OrderTable orderTable1 = new OrderTable(0, true);
        OrderTable orderTable2 = new OrderTable(0, true);
        ReflectionTestUtils.setField(orderTable1, "id", 1L);
        ReflectionTestUtils.setField(orderTable2, "id", 2L);

        given(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L)))
                .willReturn(
                        Arrays.asList(
                                orderTable1,
                                orderTable2
                        )
                );

        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        ReflectionTestUtils.setField(tableGroup, "id", 1L);
        tableGroup.addOrderTables(new OrderTable(0, true));
        tableGroup.addOrderTables(new OrderTable(0, true));
        given(tableGroupRepository.save(any())).willReturn(tableGroup);

        //when
        List<OrderTableIdRequest> orderTables = new ArrayList<>();
        orderTables.add(new OrderTableIdRequest(1L));
        orderTables.add(new OrderTableIdRequest(2L));

        TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTables);
        tableGroupRequest.setOrderTables(orderTables);
        TableGroupResponse savedTableGroup = tableGroupService.create(tableGroupRequest);

        //then
        verify(tableGroupRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getId()).isNull();
        assertThat(argumentCaptor.getValue().getOrderTables().size()).isEqualTo(2);

        assertThat(savedTableGroup.getId()).isEqualTo(1L);
        assertThat(savedTableGroup.getOrderTables().size()).isEqualTo(2);
    }

    @DisplayName("이미 등록한 주문 테이블 목록이 없거나 1개라면 단체 지정할 수 없다.")
    @Test
    void create2() {
        //given
        List<OrderTableIdRequest> orderTables = new ArrayList<>();
        orderTables.add(new OrderTableIdRequest(1L));

        TableGroupRequest newTableGroup = new TableGroupRequest(orderTables);

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(newTableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정할 주문 테이블은 2개 이상이어야 합니다.");
    }

    @DisplayName("이미 단체 지정된 것을 풀 수 있다.")
    @Test
    void ungroup1() {
        //given
        OrderTable orderTable1 = new OrderTable(0, true);
        OrderTable orderTable2 = new OrderTable(0, true);
        ReflectionTestUtils.setField(orderTable1, "id", 1L);
        ReflectionTestUtils.setField(orderTable2, "id", 2L);

        given(orderTableRepository.findAllByTableGroupId(any()))
                .willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
                .willReturn(false);

        //when
        tableGroupService.ungroup(1L);

        //then
        verify(orderTableRepository, times(2)).save(any());
        verify(orderTableRepository).save(Arrays.asList(orderTable1, orderTable2).get(0));
        verify(orderTableRepository).save(Arrays.asList(orderTable1, orderTable2).get(1));
    }

    @DisplayName("지정한 주문 테이블들이 모두 완료상태여야 그룹 해제가 가능합니다.")
    @Test
    void ungroup2() {
        //given
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
                .willReturn(true);

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지정한 주문 테이블들이 모두 완료상태여야 그룹 해제가 가능합니다.");
    }
}
