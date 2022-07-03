package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderTableRepository orderTableRepository;
    @Mock
    TableGroupRepository tableGroupRepository;

    @InjectMocks
    TableGroupService tableGroupService;

    OrderTable orderTable1;
    OrderTable orderTable2;
    OrderTables orderTables;
    TableGroup tableGroup;


    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable();
        orderTable1.setId(1L);

        orderTable2 = new OrderTable();
        orderTable2.setId(2L);

        orderTables = new OrderTables();
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);

        tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(orderTables);
    }

    @Test
    @DisplayName("빈 테이블 단체 지정 시 예외 처리")
    public void emptyTableSetTableGroupException() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(new OrderTables());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("2개 미만 테이블 단체 지정 시 예외 처리")
    public void setTableGroupLessThan2() {
        TableGroup tableGroup = new TableGroup();
        OrderTables orderTables = new OrderTables();
        orderTables.add(new OrderTable());
        tableGroup.setOrderTables(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 요청한 테이블 수와 저장된 단체의 테이블 수 불일치 시 예외 처리")
    public void createOrderTableCountMisMatchC() {
        OrderTables orderTables = new OrderTables();
        orderTables.add(new OrderTable());

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비지 않은 테이블은 단체로 지정 불가")
    public void createTableGroupNonEmptyOrderTable() {
        orderTable1.setEmpty(false);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 단체 지정된 테이블은 단체로 지정 불가")
    public void createAlreadyInGroup() {
        orderTable1.setTableGroup(tableGroup);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블은 단체 지정 정상 처리")
    public void createTableGroupSuccess() {
        orderTable1.setEmpty(true);
        orderTable2.setEmpty(true);

        given(tableGroupRepository.save(tableGroup)).willReturn(tableGroup);
        assertThat(tableGroupService.create(tableGroup).getId()).isEqualTo(tableGroup.getId());
    }

    @Test
    @DisplayName("cooking이나 meal 상태인 테이블이 있으면 단체 해제 불가")
    public void cookingMealChangeStatus() {

        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(orderTable1.getId(), orderTable2.getId()),
                Arrays.asList(COOKING.name(), MEAL.name()))).willReturn(true);
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId())).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 해제 정상처리")
    public void changeStatusSuccess() {
        orderTable1.setEmpty(true);
        orderTable2.setEmpty(true);

        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);

        tableGroupService.ungroup(tableGroup.getId());
        assertThat(orderTable1.getTableGroup()).isNull();
        assertThat(orderTable2.getTableGroup()).isNull();
    }
}