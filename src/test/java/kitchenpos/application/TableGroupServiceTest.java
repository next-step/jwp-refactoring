package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

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

    @DisplayName("주문 테이블 단체 지정을 등록한다.")
    @Test
    void create() {
        final OrderTable orderTable1 = new OrderTable(1L, null, 5, true);
        final OrderTable orderTable2 = new OrderTable(2L, null, 5, true);
        final List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        final TableGroup tableGroup = new TableGroup(1L, null, orderTables);
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);
        given(tableGroupDao.save(any())).willReturn(tableGroup);

        final OrderTable savedOrderTable1 = new OrderTable(1L, 1L, 5, false);
        final OrderTable savedOrderTable2 = new OrderTable(2L, 1L, 5, false);
        given(orderTableDao.save(any())).willReturn(savedOrderTable1);
        given(orderTableDao.save(any())).willReturn(savedOrderTable2);

        final TableGroup actual = tableGroupService.create(tableGroup);

        assertAll(
                () -> assertThat(actual).isEqualTo(tableGroup),
                () -> assertThat(actual.getOrderTables()).hasSize(2)
        );
    }

    @DisplayName("주문 테이블 단체 지정을 해제한다.")
    @Test
    void ungroup() {
        final OrderTable orderTable1 = new OrderTable(1L, 1L, 5, false);
        final OrderTable orderTable2 = new OrderTable(2L, 1L, 5, false);
        final List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);

        final OrderTable savedOrderTable1 = new OrderTable(1L, null, 5, false);
        final OrderTable savedOrderTable2 = new OrderTable(2L, null, 5, false);
        given(orderTableDao.save(any())).willReturn(savedOrderTable1);
        given(orderTableDao.save(any())).willReturn(savedOrderTable2);

        tableGroupService.ungroup(1L);

        assertAll(
                () -> assertThat(savedOrderTable1.getTableGroupId()).isNull(),
                () -> assertThat(savedOrderTable2.getTableGroupId()).isNull()
        );
    }
}
