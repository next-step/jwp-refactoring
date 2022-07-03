package kitchenpos.tableGroup.application;


import kitchenpos.order.domain.OrderRepository;
import kitchenpos.orderTable.domain.OrderTable;
import kitchenpos.orderTable.domain.OrderTableRepository;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.domain.TableGroupRepository;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static kitchenpos.orderTable.domain.OrderTableTest.주문_태이블_생성;
import static kitchenpos.tableGroup.domain.TableGroupTest.단체_지정_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Spy
    private TableGroupRepository tableGroupRepository;
    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정을 생성한다.")
    @Test
    void create() {
        // given
        OrderTable orderTable1 = 주문_태이블_생성(1L, null, 1, true);
        OrderTable orderTable2 = 주문_태이블_생성(2L, null, 1, true);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTable1.getId(), orderTable2.getId()));

        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(tableGroupRepository.save(any())).thenReturn(
                단체_지정_생성(1L, Arrays.asList(orderTable1, orderTable2))
        );

        // when
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        // then
        Assertions.assertAll(
                () -> assertThat(tableGroupResponse.getTableGroupId()).isNotNull(),
                () -> assertThat(tableGroupResponse.getOrderTableIds()).containsExactly(orderTable1.getId(), orderTable2.getId())
        );
    }

    @DisplayName("주문 테이블이 2개 이상이여야 한다.")
    @Test
    void create1() {
        // given
        OrderTable orderTable = 주문_태이블_생성(1L, null, 1, true);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Collections.singletonList(orderTable.getId()));

        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Collections.singletonList(orderTable));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("요청한 주문 테이블수와 조회한 테이블 수가 일치해야한다.")
    @Test
    void create2() {
        // given
        OrderTable orderTable1 = 주문_태이블_생성(1L, null, 1, true);
        OrderTable orderTable2 = 주문_태이블_생성(2L, null, 1, true);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTable1.getId(), orderTable2.getId()));

        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Collections.singletonList(orderTable1));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블은 비여있는 상태여야한다.")
    @Test
    void create3() {
        // given
        OrderTable orderTable1 = 주문_태이블_생성(1L, null, 1, false);
        OrderTable orderTable2 = 주문_태이블_생성(2L, null, 1, false);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTable1.getId(), orderTable2.getId()));

        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블은 단체 지정이 되어 있으면 안된다.")
    @Test
    void create4() {
        // given
        TableGroup tableGroup = 단체_지정_생성(2L, null);
        OrderTable orderTable1 = 주문_태이블_생성(1L, tableGroup, 1, false);
        OrderTable orderTable2 = 주문_태이블_생성(2L, tableGroup, 1, false);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTable1.getId(), orderTable2.getId()));

        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해체한다.")
    @Test
    void ungroup() {
        // given
        Long tableGroupId = 1L;
        OrderTable orderTable1 = 주문_태이블_생성(1L, null, 1, false);
        OrderTable orderTable2 = 주문_태이블_생성(2L, null, 1, false);
        TableGroup tableGroup = 단체_지정_생성(tableGroupId, Arrays.asList(orderTable1, orderTable2));

        when(tableGroupRepository.findWithOrderTablesById(any())).thenReturn(Optional.of(tableGroup));
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);

        // when, then
        tableGroupService.ungroup(tableGroupId);
    }

    @DisplayName("'조리', '식사' 상태 주문 존재하면 안된다.")
    @Test
    void ungroup1() {
        Long tableGroupId = 1L;
        OrderTable orderTable1 = 주문_태이블_생성(1L, null, 1, false);
        OrderTable orderTable2 = 주문_태이블_생성(2L, null, 1, false);
        TableGroup tableGroup = 단체_지정_생성(tableGroupId, Arrays.asList(orderTable1, orderTable2));

        when(tableGroupRepository.findWithOrderTablesById(any())).thenReturn(Optional.of(tableGroup));
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
