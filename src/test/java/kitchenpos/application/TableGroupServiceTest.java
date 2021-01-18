package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;

@DisplayName("단체 지정 서비스에 관련한 기능")
@SpringBootTest
class TableGroupServiceTest {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private TableService tableService;

    private OrderTable orderTable1;
    private OrderTable orderTable2;
    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(null);

        orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);
        orderTable2.setTableGroupId(null);

        tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
    }

    @DisplayName("`단체 지정`을 생성한다.")
    @Test
    void createTableGroup() {
        // Given
        OrderTableResponse savedOrderTable1 = tableService.create(new OrderTableRequest(3, true));
        OrderTableResponse savedOrderTable2 = tableService.create(new OrderTableRequest(5, true));
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(savedOrderTable1.toOrderTable(), savedOrderTable2.toOrderTable()));

        // When
        TableGroup actual = tableGroupService.create(tableGroup);
        OrderTable orderTable1 = tableService.findById(savedOrderTable1.getId());
        OrderTable orderTable2 = tableService.findById(savedOrderTable2.getId());

        // Then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getOrderTables()).containsAnyElementsOf(Arrays.asList(orderTable1, orderTable2)),
                () -> assertThat(actual.getCreatedDate()).isNotNull()
        );
    }

    @DisplayName("`단체 지정`으로 등록할 `주문 테이블`이 2개 미만이면 생성할 수 없다.")
    @Test
    void exceptionToCreateTableGroupWithZeroOrOneOrderTable() {
        // Given
        OrderTableResponse savedOrderTable = tableService.create(new OrderTableRequest(3, true));
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Collections.singletonList(savedOrderTable.toOrderTable()));

        // When & Then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("`단체 지정`할 `주문 테이블`은 비어있어야한다.")
    @Test
    void exceptionToCreateTableGroupWithNonemptyOrderTable() {
        // Given
        OrderTableResponse savedOrderTable1 = tableService.create(new OrderTableRequest(3, false));
        OrderTableResponse savedOrderTable2 = tableService.create(new OrderTableRequest(5, false));
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(savedOrderTable1.toOrderTable(), savedOrderTable2.toOrderTable()));

        // When & Then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("`단체 지정`할 `주문 테이블`이 `단체 지정`되어있으면 생성할 수 없다.")
    @Test
    void exceptionToCreateTableGroupWithRegisteredOrderTable() {
        // Given
        orderTable1.setTableGroupId(1L);
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
        given(orderTableDao.findAllByIdIn(Arrays.asList(orderTable1.getId(), orderTable2.getId())))
                .willReturn(Arrays.asList(orderTable1, orderTable2));

        // When & Then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("`단체 지정`을 해제한다.")
    @Test
    void ungroupTableGroup() {
        // Given
        orderTable1.setTableGroupId(1L);
        orderTable2.setTableGroupId(1L);
        given(orderTableDao.findAllByTableGroupId(tableGroup.getId()))
                .willReturn(Arrays.asList(orderTable1, orderTable2));
        List<Long> orderTableIds = Arrays.asList(orderTable1.getId(), orderTable2.getId());
        List<String> orderStatuses = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses)).willReturn(false);

        // When
        tableGroupService.ungroup(tableGroup.getId());

        // Then
        assertAll(
                () -> assertNull(orderTable1.getTableGroupId()),
                () -> assertNull(orderTable2.getTableGroupId())
        );
    }

    @DisplayName("`단체 지정`된 `주문 테이블`에서 `주문 상태`가 'COOKING' 이나 'MEAL' 이면 해제할 수 없다.")
    @Test
    void exceptionToUn() {
        // Given
        orderTable1.setTableGroupId(1L);
        orderTable2.setTableGroupId(1L);
        given(orderTableDao.findAllByTableGroupId(tableGroup.getId()))
                .willReturn(Arrays.asList(orderTable1, orderTable2));
        List<Long> orderTableIds = Arrays.asList(orderTable1.getId(), orderTable2.getId());
        List<String> orderStatuses = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses)).willReturn(true);

        // When & Then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
