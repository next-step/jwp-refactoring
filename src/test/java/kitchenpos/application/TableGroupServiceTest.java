package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


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

    @Test
    @DisplayName("개별 주문 테이블이 2개 미만일 경우 단체석으로 지정할 수 없다.")
    void isLimitOrderTable() {
        //given
        TableGroup tableGroup = new TableGroup(Collections.singletonList(OrderTable.createOrderTable()));

        //when & then
        assertThatIllegalArgumentException().isThrownBy(() ->
            tableGroupService.create(tableGroup)
        );
    }

    @Test
    @DisplayName("단체지정할 주문 테이블이 존재하지 않으면 단체석으로 지정할 수 없다.")
    void isNotExistOrderTable() {
        //given
        TableGroup tableGroup = new TableGroup(Arrays.asList(OrderTable.createOrderTable(), OrderTable.createOrderTable()));
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(new ArrayList<>());

        //when & then
        assertThatIllegalArgumentException().isThrownBy(() ->
                tableGroupService.create(tableGroup)
        );

    }

    @Test
    @DisplayName("단체지정할 주문 테이블이 없으면(빈 테이블) 단체석으로 지정할 수 없다.")
    void isEmptyOrderTable() {
        //given
        OrderTable orderTable1 = new OrderTable(1L,1, false);
        OrderTable orderTable2 = new OrderTable(1L,1, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroup tableGroup = new TableGroup(orderTables);

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);

        //when & then
        assertThatIllegalArgumentException().isThrownBy(() ->
                tableGroupService.create(tableGroup)
        );
    }

    @Test
    @DisplayName("단체석이 생성된다.")
    void tableGroupCreate() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, null, 1, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 1, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroup tableGroup = new TableGroup(orderTables);

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);
        given(tableGroupDao.save(tableGroup)).willReturn(tableGroup);
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);
        given(orderTableDao.save(orderTable2)).willReturn(orderTable2);

        //when
        final TableGroup saveTableGroup = tableGroupService.create(tableGroup);

        //then
        assertThat(saveTableGroup.getOrderTables()).contains(orderTable1, orderTable2);
    }

    @Test
    @DisplayName("주문 테이블중 조리중인 경우에 단체석을 개인 주문테이블로 변경할 수 없다.")
    void isNotAbleUngroup() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, null, 1, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 1, true);
        TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable1, orderTable2));

        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(tableGroup.getOrderTables());
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L, 2L),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        //when & then
        assertThatIllegalArgumentException().isThrownBy(() ->
                tableGroupService.ungroup(1L)
        );
    }

    @Test
    @DisplayName("개별 테이블로 변경된다.")
    void unGroup() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, null, 1, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 1, true);
        TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable1, orderTable2));

        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(tableGroup.getOrderTables());
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L, 2L),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);

        //when & then
        tableGroupService.ungroup(1L);
    }


}