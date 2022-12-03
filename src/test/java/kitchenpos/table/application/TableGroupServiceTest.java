package kitchenpos.table.application;

import kitchenpos.order.domain.OrderDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static kitchenpos.table.application.TableGroupService.ORDER_STATUS_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TableGroupService")
@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {

        OrderTable orderTable = orderTableDao.save(new OrderTable());

        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(orderTable);
        tableGroup = tableGroupDao.save(new TableGroup(orderTables));
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void unGroup_success() {

        for (OrderTable orderTable : tableGroup.getOrderTables()) {
            OrderTable find = orderTableDao.findById(orderTable.getId()).orElseThrow(NoSuchElementException::new);
            assertThat(find.getTableGroupId()).isNotNull();
        }

        tableGroupService.ungroup(tableGroup.getId());

        for (OrderTable orderTable : tableGroup.getOrderTables()) {
            OrderTable find = orderTableDao.findById(orderTable.getId()).orElseThrow(NoSuchElementException::new);
            assertThat(find.getTableGroupId()).isNull();
        }
    }

    @DisplayName("테이블 그룹을 해제한다. / 요리중일 경우 해제할 수 없다.")
    @Test
    void unGroup_fail_cooking() {

        for (OrderTable orderTable : tableGroup.getOrderTables()) {
            OrderTable find = orderTableDao.findById(orderTable.getId()).orElseThrow(NoSuchElementException::new);
            assertThat(find.getTableGroupId()).isNotNull();
        }

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_STATUS_EXCEPTION_MESSAGE);

    }

    @DisplayName("테이블 그룹을 해제한다. / 식사중일 경우 해제할 수 없다.")
    @Test
    void unGroup_fail_meal() {

    }
}
