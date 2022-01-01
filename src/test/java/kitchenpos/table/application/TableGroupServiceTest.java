package kitchenpos.table.application;

import kitchenpos.order.domain.FakeOrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.exception.NotCreateTableGroupException;
import kitchenpos.table.exception.NotCreatedOrderTablesException;
import kitchenpos.table.exception.NotValidOrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class TableGroupServiceTest {
    private final FakeOrderRepository orderRepository = new FakeOrderRepository();
    private final FakeOrderTableRepository orderTableRepository = new FakeOrderTableRepository(orderRepository);
    private final FakeTableGroupRepository tableGroupRepository = new FakeTableGroupRepository(orderTableRepository);
    private final TableGroupValidator tableGroupValidator = new TableGroupValidator();
    private final TableGroupService tableGroupService = new TableGroupService(tableGroupValidator, orderTableRepository, tableGroupRepository);

    private Long 저장안된_주문테이블ID_ONE = 1L;
    private Long 저장안된_주문테이블ID_TWO = 2L;

    @DisplayName("주문 테이블 수가 2보다 작으면 단체를 지정할 수 없다.")
    @Test
    void notCreateTabeGroupLessTwoTable() {
        TableGroupRequest tableGroup = TableGroupRequest.of(Arrays.asList(저장안된_주문테이블ID_ONE));
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(NotCreateTableGroupException.class);
    }

    @DisplayName("주문 테이블이 저장되어 있지 않으면 단체를 지정할 수 없다.")
    @Test
    void notCreateTableGroupNotSavedOrderTable() {
        TableGroupRequest tableGroup = TableGroupRequest.of(Arrays.asList(저장안된_주문테이블ID_ONE, 저장안된_주문테이블ID_TWO));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(NotCreatedOrderTablesException.class);
    }

    @DisplayName("주문 테이블이 공석이 아니면 단체를 지정할 수 없다.")
    @Test
    void notCreateTableGroupNotEmptyTable() {
        OrderTable savedOrderTable1 = orderTableRepository.save(OrderTable.of(10, false));
        OrderTable savedOrderTable2 = orderTableRepository.save(OrderTable.of(20, false));
        TableGroupRequest tableGroup = TableGroupRequest.of(Arrays.asList(savedOrderTable1.getId(), savedOrderTable2.getId()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(NotCreateTableGroupException.class);
    }

    @DisplayName("주문 테이블이 이미 단체 지정 되어 있으면 단체를 지정할 수 없다.")
    @Test
    void notCreateTableGroupAlreadyGroupingTable() {
        OrderTable savedOrderTable1 = orderTableRepository.save(OrderTable.of(TableGroup.of(1L), 10, true));
        OrderTable savedOrderTable2 = orderTableRepository.save(OrderTable.of(TableGroup.of(1L), 20, true));
        TableGroupRequest tableGroup = TableGroupRequest.of(Arrays.asList(savedOrderTable1.getId(), savedOrderTable2.getId()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(NotCreateTableGroupException.class);
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
                        assertThat(orderTable.getTableGroupId()).isNotNull();
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

        Order order1 = createOrder(1L, savedOrderTable1, OrderStatus.COOKING);
        orderRepository.save(order1);
        Order order2 = createOrder(2L, savedOrderTable2, OrderStatus.MEAL);
        orderRepository.save(order2);

        assertThatThrownBy(() -> tableGroupService.ungroup(result.getId()))
                .isInstanceOf(NotValidOrderException.class);
    }

    @DisplayName("단체 해지 성공")
    @Test
    void successUngroup() {
        TableGroup tableGroup1 = new TableGroup(1L);
        OrderTable 주문테이블1 = orderTableRepository.save(new OrderTable(1L, null, 10, true));
        OrderTable 주문테이블2 = orderTableRepository.save(new OrderTable(2L, null, 20, true));
        OrderTables orderTables = new OrderTables(
                Arrays.asList(
                        주문테이블1,
                        주문테이블2
                )
        );
        orderTables.assignTable(tableGroup1);

        Order order1 = createOrder(1L, 주문테이블1, OrderStatus.COMPLETION);
        orderRepository.save(order1);
        Order order2 = createOrder(2L, 주문테이블2, OrderStatus.COMPLETION);
        orderRepository.save(order2);

        tableGroupService.ungroup(1L);

        OrderTable resultOrderTable1 = orderTableRepository.findById(주문테이블1.getId()).get();
        OrderTable resultOrderTable2 = orderTableRepository.findById(주문테이블2.getId()).get();

        assertAll(
                () -> assertThat(resultOrderTable1.getTableGroup()).isNull(),
                () -> assertThat(resultOrderTable2.getTableGroup()).isNull()
        );
    }

    private Order createOrder(Long orderId, OrderTable savedOrderTable, OrderStatus completion) {
        return new Order(
                orderId,
                savedOrderTable,
                completion.name(),
                LocalDateTime.now(),
                Arrays.asList(OrderLineItem.of(null, 20))
        );
    }

}
