package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import kitchenpos.order.infrastructure.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableIdRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.infrastructure.OrderTableRepository;
import kitchenpos.table.infrastructure.TableGroupRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("주문 테이블이 하나인 경우, 단체지정할 수 없다.")
    void createWithOneOrderTable() {
        OrderTableIdRequest orderTableIdRequest = new OrderTableIdRequest(1L);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Lists.list(orderTableIdRequest));

        assertThatThrownBy(() -> {
            tableGroupService.create(tableGroupRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 없는 경우, 단체지정할 수 없다.")
    void createWithNoExistingOrderTables() {
        OrderTableIdRequest orderTableIdRequest = new OrderTableIdRequest(1L);
        OrderTableIdRequest otherOrderTableIdRequest = new OrderTableIdRequest(2L);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Lists.list(orderTableIdRequest, otherOrderTableIdRequest));
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Lists.emptyList());

        assertThatThrownBy(() -> {
            tableGroupService.create(tableGroupRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 단체 지정된 경우, 단체지정할 수 없다.")
    void createWithAlreadyGrouped() {
        OrderTableIdRequest orderTableIdRequest = new OrderTableIdRequest(1L);
        OrderTableIdRequest otherOrderTableIdRequest = new OrderTableIdRequest(2L);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Lists.list(orderTableIdRequest, otherOrderTableIdRequest));
        OrderTable table = OrderTable.of(4, true);
        OrderTable secondTable = OrderTable.of(2, true);
        TableGroup tableGroup = new TableGroup();
        table.registerGroupTable(tableGroup);
        secondTable.registerGroupTable(tableGroup);
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Lists.list(table, secondTable));

        assertThatThrownBy(() -> {
            tableGroupService.create(tableGroupRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블이 아닌 경우, 단체지정할 수 없다.")
    void createWithNotEmptyTable() {
        OrderTableIdRequest orderTableIdRequest = new OrderTableIdRequest(1L);
        OrderTableIdRequest otherOrderTableIdRequest = new OrderTableIdRequest(2L);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Lists.list(orderTableIdRequest, otherOrderTableIdRequest));
        OrderTable table = OrderTable.of(4, false);
        OrderTable secondTable = OrderTable.of(2, false);
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Lists.list(table, secondTable));

        assertThatThrownBy(() -> {
            tableGroupService.create(tableGroupRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체지정할 수 있다.")
    void create() {
        OrderTableIdRequest orderTableIdRequest = new OrderTableIdRequest(1L);
        OrderTableIdRequest otherOrderTableIdRequest = new OrderTableIdRequest(2L);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Lists.list(orderTableIdRequest, otherOrderTableIdRequest));
        OrderTable table = OrderTable.of(4, true);
        OrderTable secondTable = OrderTable.of(2, true);
        TableGroup tableGroup = new TableGroup();
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Lists.list(table, secondTable));
        when(tableGroupRepository.save(any())).thenReturn(tableGroup);

        tableGroupService.create(tableGroupRequest);

        assertThat(table.getTableGroup()).isEqualTo(tableGroup);
        assertThat(secondTable.getTableGroup()).isEqualTo(tableGroup);
    }

    @Test
    @DisplayName("단체 지정을 해제할 수 있다.")
    void unGroup() {
        OrderTable table = OrderTable.of(4, true);
        OrderTable secondTable = OrderTable.of(2, true);
        when(orderTableRepository.findAllByTableGroupId(any())).thenReturn(Lists.list(table, secondTable));

        tableGroupService.ungroup(1L);

        assertThat(table.getTableGroup()).isNull();
        assertThat(secondTable.getTableGroup()).isNull();
    }
}
