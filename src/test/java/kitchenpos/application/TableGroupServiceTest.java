package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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
    private OrderTable orderTable;
    private OrderTable orderTable2;
    private List<OrderTable> dummyOrderTableList;


    @BeforeEach
    void setUp() {
        tableGroup = new TableGroup();
        orderTable = new OrderTable();
        orderTable2 = new OrderTable();
        orderTable.setId(1L);
        orderTable2.setId(2L);
        dummyOrderTableList = Lists.list(orderTable, orderTable2);
    }

    @Test
    @DisplayName("단체 지정된 테이블 일 경우에는 적어도 2개 이상의 주문 테이블을 가져야 한다.")
    void exception_create_test() {
        tableGroup.setOrderTables(Lists.list(orderTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("should have over 2 orderTables");
    }

    @Test
    @DisplayName("단체 지정 시점에, 주문 테이블이 단체 지정시 주문받은 주문 테이블과 숫자가 맞지 않으면 생성될 수 없다.")
    void exception_orderTable() {
        tableGroup.setOrderTables(Lists.list(orderTable, orderTable2));

        given(orderTableDao.findAllByIdIn(Lists.list(1L, 2L)))
                .willReturn(new ArrayList<>());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not same as orderTable size");
    }

    @Test
    @DisplayName("단체 지정 시점에, 시점에 주문 테이블이 빈 테이블이 아니라면 단체 지정을 할 수 없다.")
    void exception2_orderTable() {
        orderTable.setEmpty(false);

        tableGroup.setOrderTables(Lists.list(orderTable, orderTable2));

        List<OrderTable> list = Lists.list(orderTable, orderTable2);
        given(orderTableDao.findAllByIdIn(Lists.list(1L, 2L)))
                .willReturn(list);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("savedOrderTable");
    }

    @Test
    @DisplayName("단체 지정을 취소할 수 있다.")
    void ungroup() {
        List<OrderTable> list = Lists.list(orderTable, orderTable2);

        given(orderTableDao.findAllByTableGroupId(1L))
                .willReturn(list);

        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Lists.list(1L, 2L),
                Lists.list(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(false);

        tableGroupService.ungroup(1L);

        assertThat(orderTable.getTableGroupId()).isNull();
        verify(orderTableDao).save(orderTable);

        assertThat(orderTable2.getTableGroupId()).isNull();
        verify(orderTableDao).save(orderTable2);
    }

    @Test
    @DisplayName("주문이 식사 또는 조리의 경우 단체 지정을 취소할 수 없다.")
    void exception_upgroup() {
        given(orderTableDao.findAllByTableGroupId(1L))
                .willReturn(dummyOrderTableList);

        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Lists.list(1L, 2L),
                Lists.list(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid OrderStatus");
    }
}