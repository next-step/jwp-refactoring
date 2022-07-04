package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.factory.fixture.OrderTableFixtureFactory.createOrderTable;
import static kitchenpos.factory.fixture.TableGroupFixtureFactory.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

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

    private OrderTable firstOrderTable;
    private OrderTable secondOrderTable;
    private List<OrderTable> orderTables;
    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        firstOrderTable = createOrderTable(1L, true);
        secondOrderTable = createOrderTable(2L, true);
        orderTables = Arrays.asList(firstOrderTable, secondOrderTable);
        tableGroup = createTableGroup(LocalDateTime.now(), orderTables);
    }

    @DisplayName("테이블 그룹을 생성할 수 있다.")
    @Test
    void create() {
        given(orderTableRepository.findAllByIdIn(Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId())))
                .willReturn(orderTables);
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(tableGroup);

        TableGroup result = tableGroupService.create(tableGroup);

        assertThat(result.getOrderTables()).hasSize(2);
    }

    @DisplayName("주문 테이블이 2개 이하이면 테이블 그룹을 생성할 수 없다.")
    @Test
    void create_invalid_orderTables_size() {
        tableGroup.setOrderTables(Arrays.asList());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 주문 테이블을 사용하지 않는다면 테이블 그룹을 생성할 수 없다.")
    @Test
    void create_invalid_orderTables_id() {
        given(orderTableRepository.findAllByIdIn(Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId())))
                .willReturn(Arrays.asList(firstOrderTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 주문 테이블이 비어있다면, 테이블 그룹을 생성할 수 없다.")
    @Test
    void create_empty_orderTables() {
        firstOrderTable.setEmpty(false);
        secondOrderTable.setEmpty(false);

        given(orderTableRepository.findAllByIdIn(Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId())))
                .willReturn(Arrays.asList(firstOrderTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 주문 테이블에 테이블그룹아이디가 설정되어 있다면, 테이블 그룹을 생성할 수 없다.")
    @Test
    void create_nonNull_orderTables() {
        firstOrderTable.setTableGroup(new TableGroup(1L));

        given(orderTableRepository.findAllByIdIn(Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId())))
                .willReturn(Arrays.asList(firstOrderTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제할 수 있다.")
    @Test
    void unGroup() {
        firstOrderTable.setTableGroup(new TableGroup(1L));
        secondOrderTable.setTableGroup(new TableGroup(1L));
        given(orderTableRepository.findAllByTableGroupId(1L)).willReturn(orderTables);
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(Boolean.FALSE);

        tableGroupService.ungroup(1L);

        assertThat(firstOrderTable.getTableGroup()).isNull();
    }

    @DisplayName("주문 상태가 COOKING 이거나 MEAL 이면, 테이블 그룹을 해제할 수 없다.")
    @Test
    void unGroup_invalid_orderStatus() {
        given(orderTableRepository.findAllByTableGroupId(1L)).willReturn(orderTables);
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(Boolean.TRUE);

        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
