package kitchenpos.order.application;

import kitchenpos.order.applicaiton.TableGroupService;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @InjectMocks
    private TableGroupService tableGroupService;
    private OrderTable orderTable1;
    private OrderTable orderTable2;
    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        orderTable1 = OrderTable.of(1L, 1L, 4, false);
        orderTable2 = OrderTable.of(2L, 2L, 3, false);

        tableGroup = TableGroup.of(1L, LocalDateTime.now(), orderTable1, orderTable2);
    }

    @Test
    @DisplayName("단체 지정 생성 시 주문 테이블이 비었을 경우 Exception")
    public void throwExceptionOrderTableIsEmpty() {
        TableGroup emptyTableGroup = TableGroup.of(1L, LocalDateTime.now());

        assertThatThrownBy(() -> tableGroupService.create(emptyTableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 생성 시 주문 테이블이 2개 미만일 경우 Exception")
    public void throwExceptionNumberOfOrderTableIsLessThanTwo() {
        TableGroup tableGroup = TableGroup.of(1L, LocalDateTime.now(), orderTable1);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 생성 시 주문 테이블 수와 저장된 단체 지정의 주문 테이블 수가 다를 경우 Exception")
    public void throwExceptionWhenOrderTableDataIntegrityIsViolated() {
        List<Long> orderTableIds = getOrderTableIds(tableGroup);

        given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(Arrays.asList(orderTable1));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 생성 시 주문 테이블이 비어있지 않으면 Exception")
    public void throwExceptionWhenOrderTableIsNotEmpty() {
        OrderTable orderTable1 = OrderTable.of(1L, null, 4, false);
        OrderTable orderTable2 = OrderTable.of(2L, null, 4, false);
        TableGroup tableGroup = TableGroup.of(1L, LocalDateTime.now(), orderTable1, orderTable2);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 생성 시 이미 단체 지정된 테이블은 단체로 지정 불가")
    public void canNotCreateTableGroupWhenOrderTableIsAlreadyGrouped() {
        OrderTable orderTable1 = OrderTable.of(1L, 1L, 4, false);
        OrderTable orderTable2 = OrderTable.of(2L, 2L, 4, false);
        TableGroup tableGroup = TableGroup.of(1L, LocalDateTime.now(), orderTable1, orderTable2);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 생성")
    public void createTableGroup() {
        OrderTable orderTable1 = OrderTable.of(1L, null, 4, true);
        OrderTable orderTable2 = OrderTable.of(2L, null, 4, true);
        TableGroup tableGroup = TableGroup.of(1L, LocalDateTime.now(), orderTable1, orderTable2);

        List<Long> orderTableIds = getOrderTableIds(tableGroup);

        given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(tableGroupRepository.save(tableGroup)).willReturn(tableGroup);
        assertThat(tableGroupService.create(tableGroup).getId()).isEqualTo(tableGroup.getId());
    }

    @Test
    @DisplayName("단체 지정 해제 시 주문 상태가 COOKING이거나 MEAL이면 Exception")
    public void throwExceptionWhenTryToUnGroupOrderIsCookingOrMeal() {
        OrderTable orderTable1 = OrderTable.of(1L, null, 4, true);
        OrderTable orderTable2 = OrderTable.of(2L, null, 4, true);
        TableGroup tableGroup = TableGroup.of(1L, LocalDateTime.now(), orderTable1, orderTable2);

        List<Long> orderTableIds = getOrderTableIds(tableGroup);

        given(orderTableDao.findAllByTableGroupId(tableGroup.getId())).willReturn(tableGroup.getOrderTables());
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId())).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 해제")
    public void ungroup() {
        OrderTable orderTable1 = OrderTable.of(1L, null, 4, true);
        OrderTable orderTable2 = OrderTable.of(2L, null, 4, true);
        TableGroup tableGroup = TableGroup.of(1L, LocalDateTime.now(), orderTable1, orderTable2);

        List<Long> orderTableIds = getOrderTableIds(tableGroup);

        given(orderTableDao.findAllByTableGroupId(tableGroup.getId())).willReturn(tableGroup.getOrderTables());
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                Arrays.asList(OrderStatus.COOKING.name(),OrderStatus.MEAL.name()))).willReturn(false);

        tableGroupService.ungroup(tableGroup.getId());
        assertThat(orderTable1.getTableGroupId()).isNull();
        assertThat(orderTable2.getTableGroupId()).isNull();
    }

    private static List<Long> getOrderTableIds(TableGroup tableGroup) {
        List<Long> orderTableIds = tableGroup.getOrderTables()
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        return orderTableIds;
    }
}