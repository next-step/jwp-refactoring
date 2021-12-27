package kitchenpos.application;

import kitchenpos.dao.*;
import kitchenpos.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class TableGroupServiceTest {
    private final OrderDao orderDao = new FakeOrderDao();
    private final OrderTableDao orderTableDao = new FakeOrderTableDao();
    private final TableGroupDao tableGroupDao = new FakeTableGroupDao();
    private final TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

    @DisplayName("주문 테이블 수가 2보다 작으면 단체를 지정할 수 없다.")
    @Test
    void notCreateTabeGroupLessTwoTable() {
        TableGroup tableGroup = TableGroup.of(
                Arrays.asList(
                        OrderTable.of(10, true)
                )
        );
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("주문 테이블이 저장되어 있지 않으면 단체를 지정할 수 없다.")
    @Test
    void notCreateTableGroupNotSavedOrderTable() {
        OrderTable savedOrderTable1 = OrderTable.of(10, true);
        OrderTable savedOrderTable2 = OrderTable.of(20, true);
        TableGroup tableGroup = TableGroup.of(Arrays.asList(savedOrderTable1, savedOrderTable2));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("주문 테이블이 공석이 아니면 단체를 지정할 수 없다.")
    @Test
    void notCreateTableGroupNotEmptyTable() {
        OrderTable savedOrderTable1 = orderTableDao.save(OrderTable.of(10, false));
        OrderTable savedOrderTable2 = orderTableDao.save(OrderTable.of(20, false));
        TableGroup tableGroup = TableGroup.of(Arrays.asList(savedOrderTable1, savedOrderTable2));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("주문 테이블이 이미 단체 지정 되어 있으면 단체를 지정할 수 없다.")
    @Test
    void notCreateTableGroupAlreadyGroupingTable() {
        OrderTable savedOrderTable1 = orderTableDao.save(OrderTable.of(1L, 10, true));
        OrderTable savedOrderTable2 = orderTableDao.save(OrderTable.of(1L, 20, true));
        TableGroup tableGroup = TableGroup.of(Arrays.asList(savedOrderTable1, savedOrderTable2));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("단체 지정 성공")
    @Test
    void successCreateTableGroup() {
        OrderTable savedOrderTable1 = orderTableDao.save(OrderTable.of(10, true));
        OrderTable savedOrderTable2 = orderTableDao.save(OrderTable.of(20, true));
        TableGroup tableGroup = TableGroup.of(Arrays.asList(savedOrderTable1, savedOrderTable2));

        TableGroup result = tableGroupService.create(tableGroup);
        List<OrderTable> resultOrderTables = result.getOrderTables();
        for (OrderTable orderTable : resultOrderTables) {
            assertThat(orderTable.getTableGroupId()).isEqualTo(result.getId());
            assertThat(orderTable.isEmpty()).isFalse();
        }
    }

    @DisplayName("주문 상태가 COOKING, MEAL 이면 단체 해지를 할 수 없다.")
    @Test
    void notUngroupTableCookingOrMeal() {
        OrderTable savedOrderTable1 = orderTableDao.save(OrderTable.of(10, true));
        OrderTable savedOrderTable2 = orderTableDao.save(OrderTable.of(20, true));
        TableGroup tableGroup = TableGroup.of(Arrays.asList(savedOrderTable1, savedOrderTable2));
        TableGroup result = tableGroupService.create(tableGroup);
        Order order1 = new Order(1L,
                savedOrderTable1.getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                Arrays.asList(OrderLineItem.of(1L, 20))
        );
        orderDao.save(order1);
        Order order2 = new Order(1L,
                savedOrderTable2.getId(),
                OrderStatus.MEAL.name(),
                LocalDateTime.now(),
                Arrays.asList(OrderLineItem.of(1L, 20))
        );
        orderDao.save(order2);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.ungroup(result.getId()));
    }

    @DisplayName("단체 해지 성공")
    @Test
    void successUngroup() {
        OrderTable savedOrderTable1 = orderTableDao.save(OrderTable.of(10, true));
        OrderTable savedOrderTable2 = orderTableDao.save(OrderTable.of(20, true));
        TableGroup tableGroup = TableGroup.of(Arrays.asList(savedOrderTable1, savedOrderTable2));
        TableGroup result = tableGroupService.create(tableGroup);

        assertThat(savedOrderTable1.getTableGroupId()).isEqualTo(tableGroup.getId());
        assertThat(savedOrderTable2.getTableGroupId()).isEqualTo(tableGroup.getId());

        Order order1 = new Order(1L,
                savedOrderTable1.getId(),
                OrderStatus.COMPLETION.name(),
                LocalDateTime.now(),
                Arrays.asList(OrderLineItem.of(1L, 20))
        );
        orderDao.save(order1);
        Order order2 = new Order(1L,
                savedOrderTable2.getId(),
                OrderStatus.COMPLETION.name(),
                LocalDateTime.now(),
                Arrays.asList(OrderLineItem.of(1L, 20))
        );
        orderDao.save(order2);

        tableGroupService.ungroup(result.getId());

        assertThat(savedOrderTable1.getTableGroupId()).isNull();
        assertThat(savedOrderTable2.getTableGroupId()).isNull();
    }

}
