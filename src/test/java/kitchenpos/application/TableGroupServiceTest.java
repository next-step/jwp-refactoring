package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DisplayName("단체 그룹 관련 기능 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    private TableGroup tableGroup;
    private OrderTable orderTable1;
    private OrderTable orderTable2;
    private List<OrderTable> orderTables;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderTableDao orderTableDao;

    @Mock
    TableGroupDao tableGroupDao;

    @InjectMocks
    TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        orderTables = new ArrayList<>();

        tableGroup = new TableGroup();
        tableGroup.setId(1L);

        orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setNumberOfGuests(15);
        orderTable1.setEmpty(true);

        orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setNumberOfGuests(2);
        orderTable2.setEmpty(true);

        orderTables.add(orderTable1);
        orderTables.add(orderTable2);

        tableGroup.setOrderTables(orderTables);
    }

    @DisplayName("비어있는 주문 테이블이 2개 이상일때 등록이 가능하다.")
    @Test
    void create_주문테이블_2개_미만_예외() {
        orderTables.remove(1);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(this.tableGroup));
    }

    @DisplayName("단체 지정은 중복될수 없다.")
    @Test
    void create_중복_예외() {
        orderTables.add(orderTable2);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(this.tableGroup));
    }

    @DisplayName("단체를 지정한다.")
    @Test
    void create() {
        List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(orderTables);
        given(tableGroupDao.save(tableGroup)).willReturn(tableGroup);
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);

        TableGroup tableGroup = tableGroupService.create(this.tableGroup);

        assertThat(tableGroup.getId()).isEqualTo(this.tableGroup.getId());
        assertThat(tableGroup.getOrderTables()).isEqualTo(this.tableGroup.getOrderTables());
    }

    @DisplayName("주문 테이블의 주문 상태가 조리 또는 식사인 경우 단체 지정을 해제할 수 없다.")
    @Test
    void ungroup_예외() {
        List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        orderTable1.setTableGroupId(tableGroup.getId());

        given(orderTableDao.findAllByTableGroupId(tableGroup.getId())).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()));
    }

    @DisplayName("단체를 해제한다.")
    @Test
    void ungroup() {
        List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        orderTable1.setTableGroupId(tableGroup.getId());
        orderTable2.setTableGroupId(tableGroup.getId());

        given(orderTableDao.findAllByTableGroupId(tableGroup.getId())).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);
        given(orderTableDao.save(orderTable2)).willReturn(orderTable2);

        tableGroupService.ungroup(tableGroup.getId());

        assertThat(orderTable1.getTableGroupId()).isNull();
        assertThat(orderTable2.getTableGroupId()).isNull();
    }
}