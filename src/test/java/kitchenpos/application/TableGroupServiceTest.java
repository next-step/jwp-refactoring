package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.dto.order.OrderTableId;
import kitchenpos.dto.order.TableGroupRequest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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

    private TableGroup tableGroup;
    private TableGroupRequest tableGroupRequest;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    public final long FIRST_ORDER_TABLE_ID = 1L;
    public final long SECOND_ORDER_TABLE_ID = 2L;
    private final long ANY_TABLE_GROUP_ID = 1L;

    @BeforeEach
    void setUp() {

        orderTable1 = OrderTable.of(10, false);
        ReflectionTestUtils.setField(orderTable1, "id", FIRST_ORDER_TABLE_ID);
        orderTable2 = OrderTable.of(10, false);
        ReflectionTestUtils.setField(orderTable2, "id", SECOND_ORDER_TABLE_ID);

        tableGroupRequest = new TableGroupRequest(new ArrayList<>());
    }

    @Test
    @DisplayName("단체 지정된 테이블 일 경우에는 적어도 2개 이상의 주문 테이블을 가져야 한다.")
    void exception_create_test() {

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("should have over 2 orderTables");
    }

    @Test
    @DisplayName("단체 지정 시점에, 주문 테이블이 단체 지정시 주문받은 주문 테이블과 숫자가 맞지 않으면 생성될 수 없다.")
    void exception_orderTable() {
        tableGroupRequest = new TableGroupRequest(Lists.list(new OrderTableId(1L), new OrderTableId(2L)));
        given(orderTableDao.findById(FIRST_ORDER_TABLE_ID)).willReturn(Optional.of(orderTable1));
        given(orderTableDao.findById(SECOND_ORDER_TABLE_ID)).willReturn(Optional.of(orderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not same as orderTable size");
    }

    @Test
    @DisplayName("단체 지정 시점에, 시점에 주문 테이블이 빈 테이블이 아니라면 단체 지정을 할 수 없다.")
    void exception2_orderTable() {
        tableGroupRequest = new TableGroupRequest(Lists.list(new OrderTableId(1L), new OrderTableId(2L)));
        tableGroup = TableGroup.of(Lists.list(orderTable1, orderTable2));
        given(orderTableDao.findAllByIdIn(Lists.list(FIRST_ORDER_TABLE_ID, SECOND_ORDER_TABLE_ID)))
                .willReturn(Lists.list(orderTable1, orderTable2));
        given(orderTableDao.findById(FIRST_ORDER_TABLE_ID)).willReturn(Optional.of(orderTable1));
        given(orderTableDao.findById(SECOND_ORDER_TABLE_ID)).willReturn(Optional.of(orderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("savedOrderTable");
    }

    @Test
    @DisplayName("단체 지정을 취소할 수 있다.")
    void ungroup() {
        List<OrderTable> orderTables = Lists.list(orderTable1, orderTable2);

        given(orderTableDao.findAllByTableGroupId(ANY_TABLE_GROUP_ID))
                .willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Lists.list(FIRST_ORDER_TABLE_ID, SECOND_ORDER_TABLE_ID),
                Lists.list(OrderStatus.COOKING, OrderStatus.MEAL)))
                .willReturn(false);

        tableGroupService.ungroup(ANY_TABLE_GROUP_ID);

        assertThat(orderTable1.getTableGroup()).isNull();
        verify(orderTableDao).save(orderTable1);

        assertThat(orderTable2.getTableGroup()).isNull();
        verify(orderTableDao).save(orderTable2);
    }

    @Test
    @DisplayName("주문이 식사 또는 조리의 경우 단체 지정을 취소할 수 없다.")
    void exception_upgroup() {
        List<OrderTable> orderTables = Lists.list(orderTable1, orderTable2);
        given(orderTableDao.findAllByTableGroupId(ANY_TABLE_GROUP_ID))
                .willReturn(orderTables);

        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Lists.list(FIRST_ORDER_TABLE_ID, SECOND_ORDER_TABLE_ID),
                Lists.list(OrderStatus.COOKING, OrderStatus.MEAL)))
                .willReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(ANY_TABLE_GROUP_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid OrderStatus");
    }
}