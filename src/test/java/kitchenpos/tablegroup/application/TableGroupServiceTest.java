package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.domain.TableGroupValidator;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("단체 지정 Business Object 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    OrderTableRepository orderTableRepository;
    @Mock
    TableGroupRepository tableGroupRepository;
    @Mock
    TableGroupValidator tableGroupValidator;

    @InjectMocks
    TableGroupService tableGroupService;

    private OrderTable 주문_테이블_1;
    private OrderTable 주문_테이블_2;
    private OrderTables 주문_테이블_목록;
    private Long 단체_지정_id;
    private TableGroup 단체_2;
    private OrderTable 단체_지정된_주문_테이블_1;
    private OrderTable 단체_지정된_주문_테이블_2;
    private OrderTables 단체_지정된_주문_테이블_목록;

    @BeforeEach
    void setUp() {
        주문_테이블_1 = new OrderTable(1L, null, 0, true);
        주문_테이블_2 = new OrderTable(2L, null, 0, true);
        주문_테이블_목록 = new OrderTables(Arrays.asList(주문_테이블_1, 주문_테이블_2));
        단체_지정_id = 1L;
        단체_2 = new TableGroup(단체_지정_id, null);
        단체_지정된_주문_테이블_1 = new OrderTable(1L, 단체_2.getId(), 5, false);
        단체_지정된_주문_테이블_2 = new OrderTable(2L, 단체_2.getId(), 4, false);
        단체_지정된_주문_테이블_목록 = new OrderTables(Arrays.asList(단체_지정된_주문_테이블_1, 단체_지정된_주문_테이블_2));
    }

    @DisplayName("2개의 주문 테이블에 대해 단체 지정")
    @Test
    void 단체_지정() {
        when(tableGroupValidator.getSavedOrderTablesIfValid(주문_테이블_Id_목록(주문_테이블_목록))).thenReturn(주문_테이블_목록);
        TableGroup 단체 = new TableGroup();
        when(tableGroupRepository.save(단체)).thenReturn(단체);

        TableGroupResponse 지정된_단체 = tableGroupService.create(createTableGroupRequest(주문_테이블_목록));

        assertThat(지정된_단체.getOrderTables()).hasSize(주문_테이블_목록.size());
    }

    @DisplayName("단체 지정 해제")
    @Test
    void 단체_지정_해제() {
        when(orderTableRepository.findByTableGroupId(단체_지정_id)).thenReturn(단체_지정된_주문_테이블_목록.getOrderTables());

        tableGroupService.ungroup(단체_지정_id);

        assertAll(
                () -> 단체_지정된_주문_테이블_목록.stream().forEach(주문_테이블 -> assertThat(주문_테이블.getTableGroupId()).isNull()),
                () -> assertThat(단체_지정된_주문_테이블_1.getNumberOfGuests()).isEqualTo(5),
                () -> assertThat(단체_지정된_주문_테이블_2.getNumberOfGuests()).isEqualTo(4)
        );
    }

    private static List<Long> 주문_테이블_Id_목록(OrderTables orderTables) {
        return orderTables.getOrderTableIds();
    }

    public static TableGroupRequest createTableGroupRequest(OrderTables orderTables) {
        return new TableGroupRequest(orderTables.getOrderTableIds());
    }
}