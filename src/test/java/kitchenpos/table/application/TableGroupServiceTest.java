package kitchenpos.table.application;

import kitchenpos.dao.table.OrderTableDao;
import kitchenpos.dao.table.TableGroupDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class TableGroupServiceTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private TableGroupDao tableGroupDao;

    private TableGroupService tableGroupService;
    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderRepository, orderTableDao, tableGroupDao);
    }

    @DisplayName("2개 이상의 빈 테이블을 단체로 지정할 수 있다.")
    @Test
    void groupingTables() {
        OrderTable table1 = orderTableDao.save(new OrderTable(4, true));
        OrderTable table2 = orderTableDao.save(new OrderTable(4, true));

        TableGroup save = tableGroupService.create(new TableGroup(Arrays.asList(table1, table2)));

        assertThat(save.getOrderTables().get(0).getId()).isEqualTo(table1.getId());
        assertThat(save.getOrderTables().get(1).getId()).isEqualTo(table2.getId());
    }

    @DisplayName("1개 이하 빈 테이블의 단체션 지정시 익셉")
    @Test
    void groupingOneTable() {
        OrderTable table1 = orderTableDao.save(new OrderTable(4, true));

        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(Arrays.asList(table1))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정은 중복될 수 없다.")
    @Test
    void groupingTablesDuplicated() {
        OrderTable table1 = orderTableDao.save(new OrderTable(4, true));

        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(Arrays.asList(table1, table1))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해지할 수 있다.")
    @Test
    void ungroupTables() {
        OrderTable table1 = orderTableDao.save(new OrderTable(4, true));
        OrderTable table2 = orderTableDao.save(new OrderTable(4, true));
        TableGroup save = tableGroupService.create(new TableGroup(Arrays.asList(table1, table2)));

        tableGroupService.ungroup(save.getId());

        List<OrderTable> founds = orderTableDao.findAllByIdIn(Arrays.asList(table1.getId(), table2.getId()));
        assertThat(founds).doesNotContain(table1, table2);
    }

    @DisplayName("단체 지정된 주문 테이블의 주문 상태가 조리인 경우 단체 지정을 해지할 수 없다.")
    @Test
    void cantUnGroupingWhenOrderStatus() {
        OrderTable table1 = orderTableDao.save(new OrderTable(4, true));
        OrderTable table2 = orderTableDao.save(new OrderTable(4, true));
        TableGroup save = tableGroupService.create(new TableGroup(Arrays.asList(table1, table2)));
        orderRepository.save(new Order(table1.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), null));

        assertThatThrownBy(() -> tableGroupService.ungroup(save.getId()))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("단체 지정된 주문 테이블 상태가 식사인 경우 단체 지정을 해지할 수 없다.")
    @Test
    void  cantUnGroupingWhenMealStatus() {
        OrderTable table1 = orderTableDao.save(new OrderTable(4, true));
        OrderTable table2 = orderTableDao.save(new OrderTable(4, true));
        TableGroup save = tableGroupService.create(new TableGroup(Arrays.asList(table1, table2)));
        orderRepository.save(new Order(table1.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(), null));

        assertThatThrownBy(() -> tableGroupService.ungroup(save.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}