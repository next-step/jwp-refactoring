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
import java.util.Collections;
import java.util.List;

import static kitchenpos.domain.OrderTableTest.빈자리;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문 테이블 그룹 테스트")
public class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private TableGroup tableGroup;

    @Test
    @DisplayName("테이블 그룹 생성")
    void createTest() {
        // given
        List<OrderTable> orderTables = Arrays.asList(빈자리, 빈자리);
        given(tableGroup.getOrderTables()).willReturn(orderTables);
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);
        given(tableGroupDao.save(any())).willReturn(tableGroup);
        // when
        TableGroup actual = tableGroupService.create(this.tableGroup);
        // then
        assertThat(actual).isEqualTo(tableGroup);
        verify(orderTableDao, times(2)).save(any());
    }

    @Test
    @DisplayName("주문 테이블은 2개 이상이여야 한다.")
    void orderTableCountException() {
        // given
        List<OrderTable> orderTables = Collections.singletonList(빈자리);
        given(tableGroup.getOrderTables()).willReturn(orderTables);
        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(this.tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록이 안된 주문 테이블에서는 주문 할 수 없다.")
    void notFoundOrderTables() {
        // given
        List<OrderTable> orderTables = Arrays.asList(빈자리, 빈자리);
        given(tableGroup.getOrderTables()).willReturn(orderTables);
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Collections.emptyList());
        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(this.tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹이 지정된 테이블은 주문 테이블을 생성할 수 없다.")
    void notCreateAlreadyTableGroup() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, 1L, 2, true);
        OrderTable orderTable2 = new OrderTable(1L, 1L, 2, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        given(tableGroup.getOrderTables()).willReturn(orderTables);
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);
        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(this.tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블이 아니면 주문 테이블을 생성 할 수 없다.")
    void notCreateNonEmpty() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, null, 2, false);
        OrderTable orderTable2 = new OrderTable(1L, null, 2, false);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        given(tableGroup.getOrderTables()).willReturn(orderTables);
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);
        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(this.tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹 해제")
    void ungroupTest() {
        // given
        OrderTable orderTable = mock(OrderTable.class);
        given(orderTableDao.findAllByTableGroupId(anyLong()))
                .willReturn(Collections.singletonList(orderTable));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
                .willReturn(false);
        given(orderTableDao.save(any())).willReturn(orderTable);
        // when
        tableGroupService.ungroup(1L);
        // then
        verify(orderTableDao, times(1)).save(any());
    }

    @Test
    @DisplayName("주문 상태가 `조리(COOKING)`, `식사(MEAL)`상태이면 해제 할 수 없다.")
    void notUngroupOrderStatus() {
        // given
        OrderTable orderTable = mock(OrderTable.class);
        given(orderTableDao.findAllByTableGroupId(anyLong()))
                .willReturn(Collections.singletonList(orderTable));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
                .willReturn(true);
        // when
        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
