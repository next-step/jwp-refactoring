package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
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

import static kitchenpos.common.DefaultData.주문테이블_1번_ID;
import static kitchenpos.common.DefaultData.주문테이블_2번_ID;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceUnitTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("2개 미만의 주문 테이블을 그룹화한다")
    @Test
    void testCreateTableGroupWithSingleTable() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Collections.singletonList(new OrderTable()));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("존재하지 않는 주문 테이블을 포함하여 그룹화한다")
    @Test
    void testCreateTableGroup_withNonExistentOrderTable() {
        // given
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(주문테이블_1번_ID);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(0L);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        given(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블_1번_ID, 0L)))
                .willReturn(Collections.singletonList(orderTable1));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("비어 있지 않은 주문 테이블을 포함하여 그룹화한다")
    @Test
    void testCreateTableGroup_withNotEmptyOrderTable() {
        // given
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(주문테이블_1번_ID);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(주문테이블_2번_ID);
        orderTable2.setEmpty(false);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        given(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블_1번_ID, 주문테이블_2번_ID)))
                .willReturn(Collections.singletonList(orderTable1));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("이미 그룹화된 주문 테이블을 포함하여 그룹화한다")
    @Test
    void testCreateTableGroup_withAlreadyGrouped() {
        // given
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(주문테이블_1번_ID);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(주문테이블_2번_ID);
        orderTable2.setTableGroupId(1L);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        given(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블_1번_ID, 주문테이블_2번_ID)))
                .willReturn(Collections.singletonList(orderTable1));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("주문 테이블 중 주문 상태가 조리, 식사일 때 그룹화 해제한다")
    @Test
    void testUngroupWithoutCompletionStatus() {
        // given
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(주문테이블_1번_ID);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(주문테이블_2번_ID);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(주문테이블_1번_ID, 주문테이블_2번_ID),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.ungroup(1L));
    }
}
