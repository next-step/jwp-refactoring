package kitchenpos.table.application;

import kitchenpos.order.domain.*;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
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

    private Long 저장안된_주문테이블ID_ONE = 1L;
    private Long 저장안된_주문테이블ID_TWO = 2L;

    @DisplayName("주문 테이블 수가 2보다 작으면 단체를 지정할 수 없다.")
    @Test
    void notCreateTabeGroupLessTwoTable() {
        TableGroupRequest tableGroup = TableGroupRequest.of(Arrays.asList(저장안된_주문테이블ID_ONE));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("주문 테이블이 저장되어 있지 않으면 단체를 지정할 수 없다.")
    @Test
    void notCreateTableGroupNotSavedOrderTable() {

        TableGroupRequest tableGroup = TableGroupRequest.of(Arrays.asList(저장안된_주문테이블ID_ONE, 저장안된_주문테이블ID_TWO));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("주문 테이블이 공석이 아니면 단체를 지정할 수 없다.")
    @Test
    void notCreateTableGroupNotEmptyTable() {
        OrderTable savedOrderTable1 = orderTableRepository.save(OrderTable.of(10, false));
        OrderTable savedOrderTable2 = orderTableRepository.save(OrderTable.of(20, false));
        TableGroupRequest tableGroup = TableGroupRequest.of(Arrays.asList(savedOrderTable1.getId(), savedOrderTable2.getId()));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("주문 테이블이 이미 단체 지정 되어 있으면 단체를 지정할 수 없다.")
    @Test
    void notCreateTableGroupAlreadyGroupingTable() {
        OrderTable savedOrderTable1 = orderTableRepository.save(OrderTable.of(TableGroup.of(1L), 10, true));
        OrderTable savedOrderTable2 = orderTableRepository.save(OrderTable.of(TableGroup.of(1L), 20, true));
        TableGroupRequest tableGroup = TableGroupRequest.of(Arrays.asList(savedOrderTable1.getId(), savedOrderTable2.getId()));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("단체 지정 성공")
    @Test
    void successCreateTableGroup() {
        OrderTable savedOrderTable1 = orderTableRepository.save(OrderTable.of(10, true));
        OrderTable savedOrderTable2 = orderTableRepository.save(OrderTable.of(20, true));
        TableGroupRequest tableGroup = TableGroupRequest.of(Arrays.asList(savedOrderTable1.getId(), savedOrderTable2.getId()));

        TableGroupResponse result = tableGroupService.create(tableGroup);
        List<OrderTableResponse> resultOrderTables = result.getOrderTables();
        assertAll(
                () -> {
                    for (OrderTableResponse orderTable : resultOrderTables) {
                        assertThat(orderTable.getTableGroupId()).isEqualTo(result.getId());
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
        TableGroupRequest tableGroup = TableGroupRequest.of(Arrays.asList(savedOrderTable1.getId(), savedOrderTable2.getId()));
        TableGroupResponse result = tableGroupService.create(tableGroup);
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
        TableGroupRequest tableGroup = TableGroupRequest.of(Arrays.asList(savedOrderTable1.getId(), savedOrderTable2.getId()));
        TableGroupResponse result = tableGroupService.create(tableGroup);

        assertAll(
                () -> assertThat(tableGroup.getOrderTableIds()).contains(savedOrderTable1.getId()),
                () -> assertThat(tableGroup.getOrderTableIds()).contains(savedOrderTable2.getId())
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
