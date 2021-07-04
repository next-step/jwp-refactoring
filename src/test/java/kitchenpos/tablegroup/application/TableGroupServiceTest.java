package kitchenpos.tablegroup.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.tablegroup.domain.OrderTable;
import kitchenpos.tablegroup.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("단체지정(Table group) 서비스 관련 테스트")
public class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;

    private Long tableGroup1Id = 1L;
    private OrderTable orderTable1 = new OrderTable(1L, null, 0, true);
    private OrderTable orderTable2 = new OrderTable(2L, null, 0, true);
    private List<OrderTable> tableGroup1OrderTables = Arrays.asList(orderTable1, orderTable2);

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderRepository, orderTableRepository, tableGroupRepository);
    }

    @DisplayName("단체지정을 등록할 수 있다")
    @Test
    void create() {
        TableGroupRequest tableGroup = TableGroupRequest.of(tableGroup1Id, tableGroup1OrderTables);
        TableGroup tableGroupResponse = new TableGroup(tableGroup1Id, tableGroup1OrderTables);

        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(tableGroupRepository.save(any())).thenReturn(tableGroupResponse);

        TableGroup response = tableGroupService.create(tableGroup);

        assertThat(response.getId()).isEqualTo(tableGroup1Id);
        assertThat(response.getOrderTables()).containsAll(tableGroup1OrderTables);
    }

    @DisplayName("단체지정에서 주문테이블은 2개 이상이어야 한다.")
    @Test
    void 단체지정의_주문테이블이_올바르지_않으면_등록할_수_없다_1() {
        List<OrderTable> falseOrderTables = Arrays.asList(orderTable1);
        TableGroupRequest tableGroupRequest = TableGroupRequest.of(tableGroup1Id, falseOrderTables);

        assertThatThrownBy(() -> {
            tableGroupService.create(tableGroupRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정에서 주문테이블은 비어있지 않아야 한다.")
    @Test
    void 단체지정의_주문테이블이_올바르지_않으면_등록할_수_없다_2() {
        List<OrderTable> falseOrderTables = Arrays.asList();
        TableGroupRequest tableGroupRequest = TableGroupRequest.of(tableGroup1Id, falseOrderTables);

        assertThatThrownBy(() -> {
            tableGroupService.create(tableGroupRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정의 주문테이블들은 모두 등록되어 있어야 한다.")
    @Test
    void 단체지정의_주문테이블이_올바르지_않으면_등록할_수_없다_3() {
        TableGroupRequest tableGroupRequest = TableGroupRequest.of(tableGroup1Id, tableGroup1OrderTables);

        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Arrays.asList(orderTable1));

        assertThatThrownBy(() -> {
            tableGroupService.create(tableGroupRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정에 등록하려는 주문테이블은 비어있는 상태여야 한다.")
    @Test
    void 등록되어있는_주문테이블들이_올바르지_않으면_단체지정을_등록할_수_없다_1() {
        OrderTable falseOrderTable1 = new OrderTable(1L, null, 3, false);
        OrderTable falseOrderTable2 = new OrderTable(2L, null, 4, false);
        List<OrderTable> falseOrderTables = Arrays.asList(falseOrderTable1, falseOrderTable2);

        TableGroupRequest tableGroupRequest = TableGroupRequest.of(tableGroup1Id, falseOrderTables);

        assertThatThrownBy(() -> {
            tableGroupService.create(tableGroupRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }
    
    @DisplayName("단체지정에 등록하려는 주문테이블의 단체지정 아이디는 비어있어야 한다.")
    @Test
    void 등록되어있는_주문테이블들이_올바르지_않으면_단체지정을_등록할_수_없다_2() {
        OrderTable falseOrderTable1 = new OrderTable(1L, 1L, 0, true);
        OrderTable falseOrderTable2 = new OrderTable(2L, 1L, 0, true);
        List<OrderTable> falseOrderTables = Arrays.asList(falseOrderTable1, falseOrderTable2);

        TableGroupRequest tableGroupRequest = TableGroupRequest.of(tableGroup1Id, falseOrderTables);

        assertThatThrownBy(() -> {
            tableGroupService.create(tableGroupRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정을 해제할 수 있다")
    @Test
    void ungroup() {
        TableGroup tableGroup = new TableGroup(tableGroup1Id, tableGroup1OrderTables);

        when(orderTableRepository.findAllByTableGroupId(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);

        tableGroupService.ungroup(tableGroup.getId());

        long nonNullCount = tableGroup1OrderTables.stream()
                .filter(orderTable -> Objects.nonNull(orderTable.getTableGroupId()))
                .count();
        assertThat(nonNullCount).isEqualTo(0);
    }

    @DisplayName("단체지정된 모든 주문테이블들 주문상태는 전부 계산완료 상태여야 한다.")
    @Test
    void 단체지정의_주문테이블_주문상태가_올바르지_않으면_해제할_수_없다() {
        TableGroup tableGroup = new TableGroup(tableGroup1Id, tableGroup1OrderTables);

        when(orderTableRepository.findAllByTableGroupId(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);

        assertThatThrownBy(() -> {
            tableGroupService.ungroup(tableGroup.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }

}
