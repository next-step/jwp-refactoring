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
import static org.junit.jupiter.api.Assertions.assertAll;

class TableGroupServiceTest {
    private final OrderRepository orderRepository = new FakeOrderRepository();
    private final OrderTableRepository orderTableRepository = new FakeOrderTableRepository();
    private final TableGroupRepository tableGroupRepository = new FakeTableGroupRepository();
    private final TableGroupService tableGroupService = new TableGroupService(orderRepository, orderTableRepository, tableGroupRepository);

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
        OrderTable savedOrderTable1 = orderTableRepository.save(OrderTable.of(10, false));
        OrderTable savedOrderTable2 = orderTableRepository.save(OrderTable.of(20, false));
        TableGroup tableGroup = TableGroup.of(Arrays.asList(savedOrderTable1, savedOrderTable2));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("주문 테이블이 이미 단체 지정 되어 있으면 단체를 지정할 수 없다.")
    @Test
    void notCreateTableGroupAlreadyGroupingTable() {
        OrderTable savedOrderTable1 = orderTableRepository.save(OrderTable.of(TableGroup.of(1L), 10, true));
        OrderTable savedOrderTable2 = orderTableRepository.save(OrderTable.of(TableGroup.of(1L), 20, true));
        TableGroup tableGroup = TableGroup.of(Arrays.asList(savedOrderTable1, savedOrderTable2));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("단체 지정 성공")
    @Test
    void successCreateTableGroup() {
        OrderTable savedOrderTable1 = orderTableRepository.save(OrderTable.of(10, true));
        OrderTable savedOrderTable2 = orderTableRepository.save(OrderTable.of(20, true));
        TableGroup tableGroup = TableGroup.of(Arrays.asList(savedOrderTable1, savedOrderTable2));

        TableGroup result = tableGroupService.create(tableGroup);
        List<OrderTable> resultOrderTables = result.getOrderTables();
        assertAll(
                () -> {
                    for (OrderTable orderTable : resultOrderTables) {
                        assertThat(orderTable.getTableGroup()).isEqualTo(result.getId());
                        assertThat(orderTable.isEmpty()).isFalse();
                    }
                }
        );
    }

    @DisplayName("주문 상태가 COOKING, MEAL 이면 단체 해지를 할 수 없다.")
    @Test
    void notUngroupTableCookingOrMeal() {
        OrderTable savedOrderTable1 = orderTableRepository.save(OrderTable.of(10, true));
        OrderTable savedOrderTable2 = orderTableRepository.save(OrderTable.of(20, true));
        TableGroup tableGroup = TableGroup.of(Arrays.asList(savedOrderTable1, savedOrderTable2));
        TableGroup result = tableGroupService.create(tableGroup);
        Order order1 = new Order(1L,
                savedOrderTable1,
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                Arrays.asList(OrderLineItem.of(null, 20))
        );
        orderRepository.save(order1);
        Order order2 = new Order(1L,
                savedOrderTable2,
                OrderStatus.MEAL.name(),
                LocalDateTime.now(),
                Arrays.asList(OrderLineItem.of(null, 20))
        );
        orderRepository.save(order2);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.ungroup(result.getId()));
    }

    @DisplayName("단체 해지 성공")
    @Test
    void successUngroup() {
        OrderTable savedOrderTable1 = orderTableRepository.save(OrderTable.of(10, true));
        OrderTable savedOrderTable2 = orderTableRepository.save(OrderTable.of(20, true));
        TableGroup tableGroup = TableGroup.of(Arrays.asList(savedOrderTable1, savedOrderTable2));
        TableGroup result = tableGroupService.create(tableGroup);

        assertAll(
                () -> assertThat(savedOrderTable1.getTableGroup()).isEqualTo(tableGroup.getId()),
                () -> assertThat(savedOrderTable2.getTableGroup()).isEqualTo(tableGroup.getId())
        );

        Order order1 = new Order(1L,
                savedOrderTable1,
                OrderStatus.COMPLETION.name(),
                LocalDateTime.now(),
                Arrays.asList(OrderLineItem.of(null, 20))
        );
        orderRepository.save(order1);
        Order order2 = new Order(1L,
                savedOrderTable2,
                OrderStatus.COMPLETION.name(),
                LocalDateTime.now(),
                Arrays.asList(OrderLineItem.of(null, 20))
        );
        orderRepository.save(order2);

        tableGroupService.ungroup(result.getId());

        assertAll(
                () -> assertThat(savedOrderTable1.getTableGroup()).isNull(),
                () -> assertThat(savedOrderTable2.getTableGroup()).isNull()
        );
    }

}
