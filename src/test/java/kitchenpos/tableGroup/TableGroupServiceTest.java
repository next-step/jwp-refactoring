package kitchenpos.tableGroup;

import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @InjectMocks
    TableGroupService tableGroupService;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderTableDao orderTableDao;

    @Mock
    TableGroupDao tableGroupDao;

    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private OrderTable 주문테이블3;
    private OrderTable 주문테이블4;
    private TableGroup 단체지정1;
    private TableGroup 단체지정2;

    @BeforeEach
    void setUp() {
        주문테이블1 = OrderTable.of(1L, null, 3, true);
        주문테이블2 = OrderTable.of(2L, null, 1, true);
        주문테이블3 = OrderTable.of(3L, null, 3, true);
        단체지정1 = TableGroup.of(1L, null, Arrays.asList(주문테이블1, 주문테이블2));
        단체지정2 = TableGroup.of(1L, null, Arrays.asList(주문테이블3));
    }

    @DisplayName("단체 지정 등록")
    @Test
    void createTableGroup() {
        // given
        when(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
                .thenReturn(단체지정1.getOrderTables());
        when(tableGroupDao.save(단체지정1))
                .thenReturn(단체지정1);

        when(orderTableDao.save(주문테이블1))
                .thenReturn(주문테이블1);
        when(orderTableDao.save(주문테이블2))
                .thenReturn(주문테이블2);

        // when
        TableGroup result = tableGroupService.create(단체지정1);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(단체지정1.getId()),
                () -> assertThat(result.getOrderTables()).containsExactly(주문테이블1, 주문테이블2)
        );

        List<OrderTable> list = result.getOrderTables();
        assertThat(list.stream().map(OrderTable::isEmpty).collect(Collectors.toList())).containsExactly(false, false);
    }

    @DisplayName("주문 테이블 리스트가 `2` 보다 작은 경우 등록 불가")
    @Test
    void createTableGroupAndOrderTableListSizeOne() {
        assertThatThrownBy(() -> {
            tableGroupService.create(단체지정2);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 리스트가 없는 경우 등록 불가")
    @Test
    void createTableGroupAndOrderTableNull() {
        // given
        단체지정2.setOrderTables(Collections.EMPTY_LIST);

        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(단체지정2);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 주문 테이블이 있는 경우 등록 불가")
    @Test
    void createTableGroupAndOrderTableNotSave() {
        // given
        when(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
                .thenReturn(Arrays.asList(주문테이블1));

        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(단체지정1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 리스트에 단체 지정이 되지 않은 경우 등록 불가")
    @Test
    void createTableGroupAndIsNotTableGroupId() {
        // given
        주문테이블1.setTableGroupId(1L);
        when(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
                .thenReturn(Arrays.asList(주문테이블1, 주문테이블2));

        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(단체지정1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 해제")
    @Test
    void ungroupTest() {
        // given
        주문테이블1.setTableGroupId(1L);
        주문테이블2.setTableGroupId(1L);
        when(orderTableDao.findAllByTableGroupId(단체지정1.getId()))
                .thenReturn(Arrays.asList(주문테이블1, 주문테이블2));
        when(orderTableDao.save(주문테이블1))
                .thenReturn(주문테이블1);
        when(orderTableDao.save(주문테이블2))
                .thenReturn(주문테이블2);

        // when
        tableGroupService.ungroup(단체지정1.getId());

        // then
        List<OrderTable> list = 단체지정1.getOrderTables().stream()
                .filter(it -> Objects.nonNull(it.getTableGroupId()))
                .collect(Collectors.toList());

        assertThat(list).isEmpty();
    }

    @DisplayName("주문 테이블 중 `조리`, `식사` 상태인 경우 해제 불가")
    @Test
    void ungroupAndCoolingOrMealStatus() {
        when(orderTableDao.findAllByTableGroupId(단체지정1.getId()))
                .thenReturn(Arrays.asList(주문테이블1, 주문테이블2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
        )).thenReturn(true);

        // then
        Assertions.assertThatThrownBy(() -> {
            tableGroupService.ungroup(단체지정1.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
