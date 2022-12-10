package kitchenpos.tablegroup.application;

import com.navercorp.fixturemonkey.FixtureMonkey;
import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @InjectMocks
    private TableGroupService tableGroupService;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;

    @DisplayName("테이블그룹을 추가할 경우 소속된 테이블이 없으면 예외발생")
    @Test
    public void throwsExceptionWhenNoneTable() {
        TableGroup tableGroup = FixtureMonkey.create()
                .giveMeBuilder(TableGroup.class)
                .set("orderTables", Collections.EMPTY_LIST)
                .sample();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블그룹을 추가할 경우 소속된 테이블이 2개미만이면 예외발생")
    @Test
    public void throwsExceptionWhenLessThen2Table() {
        TableGroup tableGroup = FixtureMonkey.create()
                .giveMeBuilder(TableGroup.class)
                .set("orderTables", Arrays.asList(new OrderTable[]{new OrderTable()}))
                .sample();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블그룹을 추가할 경우 등록안된 테이블이 있으면 예외발생")
    @Test
    public void throwsExceptionWhenNoneExistsTable() {
        List<OrderTable> orderTables = FixtureMonkey.create()
                .giveMeBuilder(OrderTable.class)
                .sampleList(3);
        TableGroup tableGroup = FixtureMonkey.create()
                .giveMeBuilder(TableGroup.class)
                .set("orderTables", orderTables)
                .sample();
        doReturn(orderTables.subList(0, 2))
                .when(orderTableDao)
                .findAllByIdIn(orderTables.stream().map(OrderTable::getId).collect(Collectors.toList()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블그룹을 추가할 경우 소속된 테이블이 공석이 아닌경우 예외발생")
    @Test
    public void throwsExceptionWhenNoneEmptyTable() {
        List<OrderTable> orderTables = FixtureMonkey.create()
                .giveMeBuilder(OrderTable.class)
                .set("empty", false)
                .sampleList(3);
        TableGroup tableGroup = FixtureMonkey.create()
                .giveMeBuilder(TableGroup.class)
                .set("orderTables", orderTables)
                .sample();
        doReturn(orderTables)
                .when(orderTableDao)
                .findAllByIdIn(orderTables.stream().map(OrderTable::getId).collect(Collectors.toList()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블그룹을 추가할 경우 소속된 테이블에 이미 등록된 그룹이 있으면 예외발생")
    @Test
    public void throwsExceptionWhenAlreadyHasGroup() {
        List<OrderTable> orderTables = FixtureMonkey.create()
                .giveMeBuilder(OrderTable.class)
                .set("empty", false)
                .set("tableGroupId", Arbitraries.longs().greaterOrEqual(1))
                .sampleList(3);
        TableGroup tableGroup = FixtureMonkey.create()
                .giveMeBuilder(TableGroup.class)
                .set("orderTables", orderTables)
                .sample();
        doReturn(orderTables)
                .when(orderTableDao)
                .findAllByIdIn(orderTables.stream().map(OrderTable::getId).collect(Collectors.toList()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블그룹을 추가할 경우 테이블그룹 반환")
    @Test
    public void returnTableGroup() {
        List<OrderTable> orderTables = FixtureMonkey.create()
                .giveMeBuilder(OrderTable.class)
                .set("empty", true)
                .setNull("tableGroupId")
                .sampleList(3);
        TableGroup tableGroup = FixtureMonkey.create()
                .giveMeBuilder(TableGroup.class)
                .set("orderTables", orderTables)
                .sample();
        doReturn(orderTables)
                .when(orderTableDao)
                .findAllByIdIn(orderTables.stream().map(OrderTable::getId).collect(Collectors.toList()));
        doReturn(tableGroup)
                .when(tableGroupDao)
                .save(tableGroup);
        doReturn(new OrderTable())
                .when(orderTableDao)
                .save(any(OrderTable.class));

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        List<OrderTable> savedOrderTables = savedTableGroup.getOrderTables();

        assertAll(
                () -> assertThat(savedTableGroup.getCreatedDate()).isNotNull(),
                () -> assertThat(savedOrderTables.stream().map(OrderTable::isEmpty).collect(Collectors.toList()))
                        .allMatch(isEmpty -> !isEmpty),
                () -> assertThat(savedOrderTables.stream().map(OrderTable::getTableGroupId).collect(Collectors.toList()))
                        .allMatch(tableGroupId -> tableGroupId == savedTableGroup.getId()));
    }

    @DisplayName("테이블그룹을 해제할경우 테이블이 조리중이거나 식사중이면 예외발생")
    @Test
    public void throwsExceptionWhenTableIsMillOrCOOKING() {
        List<OrderTable> orderTables = FixtureMonkey.create()
                .giveMeBuilder(OrderTable.class)
                .set("empty", true)
                .setNull("tableGroupId")
                .sampleList(3);
        List<Long> orderTableIds = orderTables.stream().map(OrderTable::getId).collect(Collectors.toList());
        TableGroup tableGroup = FixtureMonkey.create()
                .giveMeBuilder(TableGroup.class)
                .set("orderTables", orderTables)
                .sample();
        doReturn(orderTables)
                .when(orderTableDao)
                .findAllByTableGroupId(tableGroup.getId());
        doReturn(true)
                .when(orderDao)
                .existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

