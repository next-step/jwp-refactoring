package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@DisplayName("테이블 그룹 비즈니스 로직을 처리하는 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    private List<OrderTable> orderTables = new ArrayList<>();
    private OrderTable firstOrderTable;
    private OrderTable secondOrderTable;
    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        orderTables = new ArrayList<>();

        firstOrderTable = new OrderTable();
        firstOrderTable.setId(1L);
        firstOrderTable.setEmpty(true);
        firstOrderTable.setNumberOfGuests(2);

        secondOrderTable = new OrderTable();
        secondOrderTable.setId(2L);
        secondOrderTable.setEmpty(true);
        secondOrderTable.setNumberOfGuests(3);

        orderTables.add(firstOrderTable);
        orderTables.add(secondOrderTable);

        tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(orderTables);
    }

    @DisplayName("테이블 그룹(단체 지정)을 생성한다.")
    @Test
    void 테이블_그룹_생성() {
        final List<Long> orderTablesIds = tableGroup.getOrderTables().stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        given(orderTableDao.findAllByIdIn(orderTablesIds)).willReturn(orderTables);
        given(tableGroupDao.save(tableGroup)).willReturn(tableGroup);
        given(orderTableDao.save(firstOrderTable)).willReturn(firstOrderTable);

        final TableGroup createdTableGroup = tableGroupService.create(this.tableGroup);

        assertThat(createdTableGroup.getId()).isEqualTo(tableGroup.getId());
        assertThat(createdTableGroup.getOrderTables().get(0).getNumberOfGuests()).isEqualTo(tableGroup.getOrderTables().get(0).getNumberOfGuests());
        assertThat(createdTableGroup.getOrderTables().get(1).getNumberOfGuests()).isEqualTo(tableGroup.getOrderTables().get(1).getNumberOfGuests());
    }

    @DisplayName("주문 테이블이 1개 이하인 경우 테이블 그룹을 생성할 수 없다.")
    @Test
    void 주문_테이블이_1개_이하인_경우_테이블_그룹_생성() {
        final List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(firstOrderTable);

        tableGroup.setOrderTables(orderTables);

        assertThatThrownBy(() -> {
            final TableGroup createdTableGroup = tableGroupService.create(this.tableGroup);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 주문 테이블이 포함된 경우 테이블 그룹을 생성할 수 없다.")
    @Test
    void 존재하지_않는_주문_테이블이_존재하는_경우_테이블_그룹_생성() {
        final List<Long> orderTablesIds = tableGroup.getOrderTables().stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        given(orderTableDao.findAllByIdIn(orderTablesIds)).willReturn(Collections.singletonList(firstOrderTable));

        assertThatThrownBy(() -> {
            final TableGroup createdTableGroup = tableGroupService.create(this.tableGroup);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("다른 테이블 그룹에 포함된 경우 테이블 그룹을 생성할 수 없다.")
    @Test
    void 다른_테이블_그룹_포함된_경우_테이블_그룹_생성() {
        firstOrderTable.setEmpty(false);

        final List<Long> orderTablesIds = tableGroup.getOrderTables().stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        given(orderTableDao.findAllByIdIn(orderTablesIds)).willReturn(orderTables);

        assertThatThrownBy(() -> {
            final TableGroup createdTableGroup = tableGroupService.create(this.tableGroup);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void 테이블_그룹_해제() {
        final List<Long> orderTablesIds = tableGroup.getOrderTables().stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        firstOrderTable.setEmpty(false);
        firstOrderTable.setTableGroupId(1L);
        secondOrderTable.setEmpty(false);
        secondOrderTable.setTableGroupId(1L);

        given(orderTableDao.findAllByTableGroupId(tableGroup.getId())).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTablesIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
            .willReturn(false);

        tableGroupService.ungroup(tableGroup.getId());

        assertThat(firstOrderTable.getTableGroupId()).isNull();
        assertThat(secondOrderTable.getTableGroupId()).isNull();
    }

    @DisplayName("테이블 그룹의 주문 상태가 조리 또는 식사인 경우 해제할 수 없다.")
    @Test
    void 주문_상태가_조리_또는_식사인_경우_테이블_그룹_해제() {
        final List<Long> orderTablesIds = tableGroup.getOrderTables().stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        firstOrderTable.setEmpty(false);
        firstOrderTable.setTableGroupId(1L);
        secondOrderTable.setEmpty(false);
        secondOrderTable.setTableGroupId(1L);

        given(orderTableDao.findAllByTableGroupId(tableGroup.getId())).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTablesIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
            .willReturn(true);

        assertThatThrownBy(() -> {
            tableGroupService.ungroup(tableGroup.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
