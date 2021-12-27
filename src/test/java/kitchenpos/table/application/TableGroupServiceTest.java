package kitchenpos.table.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    OrderTableRepository orderTableRepository;

    @Mock
    TableGroupRepository tableGroupRepository;

    @InjectMocks
    TableGroupService tableGroupService;

    private OrderTable orderTable;

    private OrderTable orderTable2;

    private List<OrderTable> orderTables;

    private TableGroup tableGroup;

    private TableGroupRequest tableGroupRequest;

    @BeforeEach
    void setUp() {
        orderTable = OrderTable.of(1L, null, 2, false);
        orderTable2 = OrderTable.of(2L, null, 3, false);
        OrderLineItem orderLineItem = OrderLineItem.of(1L, null, null, 1);
        Order.of(orderTable, OrderStatus.COMPLETION, Arrays.asList(orderLineItem));
        Order.of(orderTable2, OrderStatus.COMPLETION, Arrays.asList(orderLineItem));
        orderTables = Arrays.asList(orderTable, orderTable2);
        tableGroup = TableGroup.of(1L, orderTables);
        tableGroupRequest = new TableGroupRequest(Arrays.asList(1L, 2L));
    }

    @DisplayName("테이블 그룹을 등록한다.")
    @Test
    void create() {
        // given
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));
        when(tableGroupRepository.save(any())).thenReturn(tableGroup);

        // when
        TableGroupResponse expected = tableGroupService.create(tableGroupRequest);

        // then
        assertThat(tableGroup.getId()).isEqualTo(expected.getId());
    }

    @DisplayName("주문테이블에 테이블 그룹을 해제한다.")
    @Test
    void ungroup() {
        // given
        when(tableGroupRepository.findById(any())).thenReturn(Optional.of(tableGroup));

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        assertThat(orderTable.getTableGroup()).isNull();
    }
}
