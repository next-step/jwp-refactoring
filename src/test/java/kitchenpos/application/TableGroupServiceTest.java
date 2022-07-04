package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.transaction.Transactional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableGroupServiceTest {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    TableGroupRepository tableGroupRepository;
    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    TableGroupService tableGroupService;

    OrderTable orderTable1;
    OrderTable orderTable2;
    OrderTables orderTables;
    TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable(null, 3, true);
        orderTable2 = new OrderTable(null, 4, true);

        orderTables = new OrderTables();
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);

        tableGroup = new TableGroup();
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
    @Transactional
    @DisplayName("비지 않은 테이블은 단체로 지정 불가")
    public void createTableGroupNonEmptyOrderTable() {
        orderTable1.setEmpty(false);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Transactional
    @DisplayName("이미 단체 지정된 테이블은 단체로 지정 불가")
    public void createAlreadyInGroup() {
        orderTable1.setTableGroup(tableGroup);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 단체 지정 정상 처리")
    public void createTableGroupSuccess() {
        orderTable1.setEmpty(true);
        orderTable2.setEmpty(true);

        assertThat(tableGroupService.create(tableGroup).getId()).isEqualTo(tableGroup.getId());
    }

    @Test
    @Transactional
    @DisplayName("cooking이나 meal 상태인 테이블이 있으면 단체 해제 불가")
    public void cookingMealChangeStatus() {
        TableGroup save = tableGroupRepository.save(tableGroup);

        orderTable1.setTableGroup(save);
        orderTable2.setTableGroup(save);
        OrderTable orderTable = save.getOrderTables().getOrderTables().get(0);

        orderRepository.save(new Order(orderTable, OrderStatus.MEAL));
        orderRepository.flush();

        orderTableRepository.save(orderTable);

        assertThatThrownBy(() -> tableGroupService.ungroup(orderTable.getId())).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @Transactional
    @DisplayName("단체 해제 정상처리")
    public void changeStatusSuccess() {
        orderTable1.setEmpty(true);
        orderTable2.setEmpty(true);

        tableGroupService.ungroup(tableGroup.getId());
        assertThat(orderTable1.getTableGroup()).isNull();
        assertThat(orderTable2.getTableGroup()).isNull();
    }
}