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

import java.util.Arrays;
import java.util.List;

import static kitchenpos.application.TableServiceTest.generateOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("단체지정")
class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private TableGroupDao tableGroupDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private OrderDao orderDao;

    private OrderTable orderTable1;
    private OrderTable orderTable2;
    private OrderTable orderTable3;
    private OrderTable orderTable4;

    List<Long> orderTableIds;

    private TableGroup tableGroup;


    @BeforeEach
    void setUp() {
        orderTable1 = generateOrderTable(1L, 5, true);
        orderTable2 = generateOrderTable(2L, 3, true);
        orderTable3 = generateOrderTable(3L, 3, false);
        orderTable4 = generateOrderTable(4L, 1L,3, false);

        orderTableIds = Arrays.asList(orderTable1.getId(), orderTable2.getId());

        tableGroup = generateTable(1L, Arrays.asList(orderTable1, orderTable2));
    }

    @Test
    @DisplayName("단체 지정을 할 수 있다.")
    void tableGroupTest1() {
        given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(tableGroupDao.save(tableGroup)).willReturn(tableGroup);
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);
        given(orderTableDao.save(orderTable2)).willReturn(orderTable2);

        TableGroup createdTableGroup = tableGroupService.create(tableGroup);
        assertThat(createdTableGroup.getId()).isEqualTo(tableGroup.getId());
    }

    @Test
    @DisplayName("단체 지정 : 주문 테이블은 필수값이며, 2개 미만이어선 안된다.")
    void tableGroupTest2() {
        TableGroup nullOrderTable = generateTable(1L, null);
        TableGroup lessThan2OrderTable = generateTable(2L, Arrays.asList(orderTable1));

        assertThatThrownBy(() -> tableGroupService.create(nullOrderTable)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> tableGroupService.create(lessThan2OrderTable)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 : 주문 테이블은 존재하는 주문 테이블들로만 구성되야 한다.")
    void tableGroupTest3() {
        given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(Arrays.asList(orderTable1));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 : 다른 테이블 그룹에 속한 주문 테이블로는 요청할 수 없다.")
    void tableGroupTest4() {
        given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(Arrays.asList(orderTable1, orderTable4));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정을 제거할 수 있다.")
    void tableGroupTest5() {
        given(orderTableDao.findAllByTableGroupId(tableGroup.getId())).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(orderTable1.getId(), orderTable2.getId()), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);
        given(orderTableDao.save(orderTable2)).willReturn(orderTable2);

        tableGroupService.ungroup(tableGroup.getId());
    }

    @Test
    @DisplayName("단체 지정 제거 : 주문 테이블의 상태가 조리중이거나 식사중이면 안된다.")
    void tableGroupTest6() {
        given(orderTableDao.findAllByTableGroupId(tableGroup.getId())).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(orderTable1.getId(), orderTable2.getId()), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId())).isInstanceOf(IllegalArgumentException.class);

    }

    public static TableGroup generateTable(long id, List<OrderTable> orderTables) {
        return TableGroup.of(id, null, orderTables);
    }

}