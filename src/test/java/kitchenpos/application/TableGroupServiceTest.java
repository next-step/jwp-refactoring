package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    private OrderTable orderTable1;

    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {

    }

    @DisplayName("테이블 그룹을 생성한다")
    @Test
    void createTest() {
        // given
        orderTable1 = new OrderTable(1L, null, 1, true);
        orderTable2 = new OrderTable(2L, null, 1, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);

        when(orderTableDao.findAllByIdIn(Arrays.asList(orderTable1.getId(), orderTable2.getId()))).thenReturn(orderTables);
        when(tableGroupDao.save(tableGroup)).thenReturn(tableGroup);

        // when
        TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
        TableGroup returnedTableGroup = tableGroupService.create(tableGroup);

        assertThat(returnedTableGroup).isEqualTo(tableGroup);
    }

    @DisplayName("테이블이 1개 있는 테이블 그룹을 생성한다")
    @Test
    void oneOrderTableCreateTest() {
        // given
        orderTable1 = new OrderTable(1L, null, 1, true);
        List<OrderTable> orderTables = Collections.singletonList(orderTable1);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);

        // when
        TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 없는 테이블 그룹을 생성한다")
    @Test
    void noOrderTableCreateTest() {
        // given
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), null);

        // when
        TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블이 아닌 테이블을 테이블 그룹으로 생성한다")
    @Test
    void noEmptyTableCreateTest() {

        // given
        orderTable1 = new OrderTable(1L, null, 1, true);
        orderTable2 = new OrderTable(2L, null, 1, false);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);

        when(orderTableDao.findAllByIdIn(Arrays.asList(orderTable1.getId(), orderTable2.getId()))).thenReturn(orderTables);

        // when
        TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹에 속해있는 테이블을 테이블 그룹으로 만든다")
    @Test
    void tableGroupTableCreateTest() {

        // given
        orderTable1 = new OrderTable(1L, 1L, 1, true);
        orderTable2 = new OrderTable(2L, null, 1, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);

        when(orderTableDao.findAllByIdIn(Arrays.asList(orderTable1.getId(), orderTable2.getId()))).thenReturn(orderTables);

        // when
        TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }
}
