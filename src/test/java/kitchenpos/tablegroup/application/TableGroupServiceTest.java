package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.error.InvalidRequestException;
import kitchenpos.common.error.InvalidOrderStatusException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.order.repository.OrderDao;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableDao;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.repository.TableGroupDao;

@ExtendWith(MockitoExtension.class)
@DisplayName("단체 지정 테스트")
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroup tableGroup;
    private OrderTable orderTable;
    private OrderTable addOrderTable;

    @BeforeEach
    void setup() {
        orderTable = new OrderTable(1L, new NumberOfGuests(2), true);
        addOrderTable = new OrderTable(2L, new NumberOfGuests(2), true);
    }

    @DisplayName("사용자는 단체 지정을 할 수 있다.")
    @Test
    void create() {
        // given

        // when
        when(orderTableDao.findAllById(any())).thenReturn(Arrays.asList(orderTable, addOrderTable));
        when(tableGroupDao.save(any())).thenReturn(tableGroup);

        TableGroupResponse createdTableGroup = tableGroupService.create(new TableGroupRequest(Arrays.asList(1L, 2L)));
        // then
        assertThat(createdTableGroup).isNotNull();
    }

    @DisplayName("사용자는 단체를 취소 할 수 있다.")
    @Test
    void ungroup() {
        // given
        List<OrderTable> orderTables = Arrays.asList(orderTable, addOrderTable);
        Order order = Order.of(1L, OrderStatus.COMPLETION);
        // when
        when(orderTableDao.findAllByTableGroupId(1L)).thenReturn(orderTables);
        when(orderDao.findByOrderTableId(any())).thenReturn(Arrays.asList(order));

        // then
        tableGroupService.ungroup(1L);
    }

    @DisplayName("주문테이블의 요청 id의 개수가 2보다 작은지 체크한다.")
    @Test
    void createFailedByOrderTables() {
        // given
        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(Arrays.asList(1L))))
                .isInstanceOf(InvalidRequestException.class);
    }

    @DisplayName("주문테이블의 데이터를 체크한다. 이 때 요청 받은 주문테이블의 데이터가 모두 있는지 체크한다.")
    @Test
    void createFailedByOrderTablesCount() {
        // given
        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest()))
                .isInstanceOf(InvalidRequestException.class);
    }

    @DisplayName("그룹 제거 시 주문 상태가 요리중, 식사중 상태가아닌지 체크한다.")
    @Test
    void ungroupFailedByCookingStatus() {
        // given
        List<OrderTable> orderTables = Arrays.asList(orderTable, addOrderTable);
        Order order = Order.of(1L, OrderStatus.MEAL);
        // when
        when(orderTableDao.findAllByTableGroupId(1L)).thenReturn(orderTables);
        when(orderDao.findByOrderTableId(any())).thenReturn(Arrays.asList(order));

        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(InvalidOrderStatusException.class);
    }
}