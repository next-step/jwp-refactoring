package kitchenpos.table.application;

import static kitchenpos.ServiceTestFactory.createOrderTableBy;
import static kitchenpos.ServiceTestFactory.createTableGroupBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.order.dao.FakeOrderDao;
import kitchenpos.table.dao.FakeOrderTableDao;
import kitchenpos.table.dao.FakeTableGroupDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableGroupServiceTest {
    private final TableGroupService tableGroupService = new TableGroupService(new FakeOrderDao(),
            new FakeOrderTableDao(), new FakeTableGroupDao());

    private OrderTable firstTable;
    private OrderTable secondTable;
    private OrderTable thirdTable;
    private OrderTable fourthTable;
    private TableGroup tableGroup;
    private TableGroup secondTableGroup;

    @BeforeEach
    void setUp() {
        firstTable = createOrderTableBy(1L, 4, true, null);
        secondTable = createOrderTableBy(2L, 3, true, null);
        thirdTable = createOrderTableBy(3L, 2, false, null);
        fourthTable = createOrderTableBy(4L, 1, false, null);
        tableGroup = createTableGroupBy(1L, Arrays.asList(firstTable, secondTable));
        secondTableGroup = createTableGroupBy(2L, Arrays.asList(thirdTable, fourthTable));
    }

    @Test
    @DisplayName("주문 테이블이 없는 경우, 단체지정할 수 없다.")
    void createWithNoExistingOrderTables() {
        TableGroup tableGroup = new TableGroup();
        //when, then
        assertThatThrownBy(() -> {
            tableGroupService.create(tableGroup);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 하나인 경우, 단체지정할 수 없다.")
    void createWithOneOrderTable() {
        //given
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = Collections.singletonList(firstTable);
        tableGroup.setOrderTables(orderTables);

        //when, then
        assertThatThrownBy(() -> {
            tableGroupService.create(tableGroup);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("저장된 주문 테이블이 아닌 경우, 단체지정할 수 없다.")
    void createWithNoSavedOrderTables() {
        //given
        TableGroup newGroup = new TableGroup();
        OrderTable table = createOrderTableBy(98L, 4, false, null);
        OrderTable anotherTable = createOrderTableBy(99L, 3, false, null);
        List<OrderTable> orderTables = Arrays.asList(table, anotherTable);
        newGroup.setOrderTables(orderTables);
        //when, then
        assertThatThrownBy(() -> {
            tableGroupService.create(newGroup);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체지정할 수 있다.")
    void create() {
        //given
        List<Long> expectedIds = findOrderTableIds(Arrays.asList(firstTable, secondTable));
        //when
        TableGroup saved = tableGroupService.create(tableGroup);
        List<Long> actualIds = findOrderTableIds(saved.getOrderTables());
        //then
        assertThat(actualIds).containsExactlyElementsOf(expectedIds);
    }

    private List<Long> findOrderTableIds(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    @Test
    @DisplayName("테이블이 조리, 식사 중이면 단체 지정을 해제할 수 없다.")
    void unGroupWithInvalidOrderStatus() {
        //given
        TableGroup saved = tableGroupService.create(secondTableGroup);
        //when, then
        assertThatThrownBy(() -> {
            tableGroupService.ungroup(saved.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정을 해제할 수 있다.")
    void unGroup() {
        //given
        TableGroup saved = tableGroupService.create(tableGroup);
        //when, then
        tableGroupService.ungroup(saved.getId());
    }
}
