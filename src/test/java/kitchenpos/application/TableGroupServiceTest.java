package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private TableGroupDao tableGroupDao;
    @Autowired
    private TableGroupService tableGroupService;

    private OrderTable orderTable1;
    private OrderTable orderTable2;
    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        super.setUp();
        orderTable1 = this.orderTableDao.save(new OrderTable(null, 0, true));
        orderTable2 = this.orderTableDao.save(new OrderTable(null, 0, true));
        tableGroup = this.tableGroupService.create(new TableGroup(Arrays.asList(orderTable1, orderTable2)));
    }

    @Test
    @DisplayName("테이블 정보가 존재하지 않을 경우 예외를 던진다.")
    void createFail_orderTableNullOrEmpty() {
        OrderTable orderTable = new OrderTable(100L, null, 0, true);

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.tableGroupService.create(new TableGroup(Collections.emptyList())));
        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.tableGroupService.create(new TableGroup(Collections.singletonList(orderTable))));
    }

    @Test
    @DisplayName("테이블 중 비어있지 않은 테이블이 존재할 경우 생성될 수 없다.")
    void createFail_orderTableEmptyStatus() {
        OrderTable orderTable = this.orderTableDao.save(new OrderTable(null, 4, false));

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.tableGroupService.create(new TableGroup(Collections.singletonList(orderTable))));
    }

    @Test
    @DisplayName("테이블 중 이미 테이블 그룹에 속한 테이블이 존재할 경우 생성될 수 없다.")
    void createFail_alreadyContainTableGroup() {
        OrderTable orderTable = this.orderTableDao.save(new OrderTable(null, 0, true));
        this.tableGroupDao.save(new TableGroup(Collections.singletonList(orderTable)));

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.tableGroupService.create(new TableGroup(Collections.singletonList(orderTable))));
    }

    @Test
    @DisplayName("테이블 그룹이 정상적으로 생성된다.")
    void createTableGroup() {
        assertThat(tableGroup.getId()).isNotNull();
        assertThat(tableGroup.getOrderTables()).hasSize(2);
        assertTrue(tableGroup.getOrderTables().stream().anyMatch(orderTable -> !orderTable.isEmpty()));
    }

    @Test
    @DisplayName("테이블 중 식사중이거나 조리중인 테이블이 있다면 해제할 수 없다.")
    void ungroupFail() {
        Order order = new Order(orderTable1.getId(), null);
        order.setOrderStatus(OrderStatus.COOKING.name());
        this.orderDao.save(order);

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.tableGroupService.ungroup(tableGroup.getId()));
    }

    @Test
    @DisplayName("테이블 그룹을 해제한다.")
    void ungroup() {
        this.tableGroupService.ungroup(tableGroup.getId());

        List<OrderTable> orderTables = this.orderTableDao.findAll();
        assertTrue(orderTables.stream().anyMatch(orderTable -> orderTable.getTableGroupId() == null));
    }

}
