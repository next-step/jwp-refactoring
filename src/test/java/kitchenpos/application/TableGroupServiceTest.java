package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
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
    OrderDao orderDao;
    @Mock
    OrderTableDao orderTableDao;
    @Mock
    TableGroupDao tableGroupDao;

    @InjectMocks
    TableGroupService tableGroupService;

    private OrderTable 주문_테이블_1;
    private OrderTable 주문_테이블_2;
    private List<OrderTable> 주문_테이블_목록;
    private Long 단체_지정_id;
    private OrderTable 단체_지정된_주문_테이블_1;
    private OrderTable 단체_지정된_주문_테이블_2;
    private List<OrderTable> 단체_지정된_주문_테이블_목록;

    @BeforeEach
    void setUp() {
        주문_테이블_1 = new OrderTable(1L, null, 0, true);
        주문_테이블_2 = new OrderTable(2L, null, 0, true);
        주문_테이블_목록 = Arrays.asList(주문_테이블_1, 주문_테이블_2);
        단체_지정_id = 1L;
        단체_지정된_주문_테이블_1 = new OrderTable(1L, 단체_지정_id, 5, false);
        단체_지정된_주문_테이블_2 = new OrderTable(2L, 단체_지정_id, 4, false);
        단체_지정된_주문_테이블_목록 = Arrays.asList(단체_지정된_주문_테이블_1, 단체_지정된_주문_테이블_2);
    }

    @DisplayName("2개의 주문 테이블에 대해 단체 지정")
    @Test
    void 단체_지정() {
        when(orderTableDao.findAllByIdIn(주문_테이블_Id_목록(주문_테이블_목록))).thenReturn(주문_테이블_목록);
        TableGroup 단체 = new TableGroup(1L, null, 주문_테이블_목록);
        when(tableGroupDao.save(단체)).thenReturn(단체);

        TableGroup 지정된_단체 = tableGroupService.create(단체);

        assertAll(
                () -> assertThat(지정된_단체.getCreatedDate()).isNotNull(),
                () -> assertThat(지정된_단체).isEqualTo(단체),
                () -> assertThat(지정된_단체.getOrderTables()).containsAll(주문_테이블_목록),
                () -> 지정된_단체.getOrderTables()
                        .forEach(orderTable -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(0))
        );
    }

    @DisplayName("2개 미만 주문 테이블에 대해 단체 지정 요청 시 예외처리")
    @Test
    void 주문_테이블_1개_단체_지정_예외처리() {
        List<OrderTable> 주문_테이블_1개 = Arrays.asList(주문_테이블_1);
        TableGroup 단체 = new TableGroup(1L, null, 주문_테이블_1개);

        assertThatThrownBy(
                () -> tableGroupService.create(단체)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 주문 테이블에 대해 단체 지정 요청 시 예외처리")
    @Test
    void 등록되지_않은_주문_테이블_예외처리() {
        List<OrderTable> 단체_지정할_주문_테이블_목록 = Arrays.asList(주문_테이블_1, 주문_테이블_2);
        List<OrderTable> 조회된_주문_테이블_목록 = Arrays.asList(주문_테이블_1);
        when(orderTableDao.findAllByIdIn(주문_테이블_Id_목록(단체_지정할_주문_테이블_목록))).thenReturn(조회된_주문_테이블_목록);
        TableGroup 단체 = new TableGroup(1L, null, 단체_지정할_주문_테이블_목록);

        assertThatThrownBy(
                () -> tableGroupService.create(단체)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블이 아닌 주문 테이블에 대해 단체 지정 요청 시 예외처리")
    @Test
    void 비어있지_않은_주문_테이블_예외처리() {
        OrderTable 비어있지_않은_테이블 = new OrderTable(3L, null, 0, false);
        List<OrderTable> 비어있지_않은_주문_테이블_목록 = Arrays.asList(비어있지_않은_테이블);
        TableGroup 단체 = new TableGroup(1L, null, 비어있지_않은_주문_테이블_목록);

        assertThatThrownBy(
                () -> tableGroupService.create(단체)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 단체 지정된 주문 테이블에 대해 단체 지정 요청 시 예외처리")
    @Test
    void 중복_단체_지정_예외처리() {
        OrderTable 단체_지정된_테이블 = new OrderTable(3L, 1L, 0, false);
        List<OrderTable> 단체_지정된_주문_테이블_포함_목록 = Arrays.asList(단체_지정된_테이블, 주문_테이블_1);
        TableGroup 단체 = new TableGroup(1L, null, 단체_지정된_주문_테이블_포함_목록);
        when(orderTableDao.findAllByIdIn(주문_테이블_Id_목록(단체_지정된_주문_테이블_포함_목록))).thenReturn(단체_지정된_주문_테이블_포함_목록);

        assertThatThrownBy(
                () -> tableGroupService.create(단체)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 해제")
    @Test
    void 단체_지정_해제() {
        when(orderTableDao.findAllByTableGroupId(단체_지정_id)).thenReturn(단체_지정된_주문_테이블_목록);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                주문_테이블_Id_목록(단체_지정된_주문_테이블_목록),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);

        tableGroupService.ungroup(단체_지정_id);

        assertAll(
                () -> 단체_지정된_주문_테이블_목록.forEach(주문_테이블 -> assertThat(주문_테이블.getTableGroupId()).isNull()),
                () -> assertThat(단체_지정된_주문_테이블_1.getNumberOfGuests()).isEqualTo(5),
                () -> assertThat(단체_지정된_주문_테이블_2.getNumberOfGuests()).isEqualTo(4)
        );
    }

    @DisplayName("계산 완료되지 않은 주문이 등록된 주문 테이블에 대해 단체 지정 해제")
    @Test
    void 단체_지정_해제_계산_미완료_예외처리() {
        when(orderTableDao.findAllByTableGroupId(단체_지정_id)).thenReturn(단체_지정된_주문_테이블_목록);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                주문_테이블_Id_목록(단체_지정된_주문_테이블_목록),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

        assertThatThrownBy(
                () -> tableGroupService.ungroup(단체_지정_id)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private List<Long> 주문_테이블_Id_목록(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}