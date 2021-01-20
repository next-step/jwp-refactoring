package kitchenpos.application;

import kitchenpos.BaseServiceTest;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.utils.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableGroupServiceTest extends BaseServiceTest {
    @Autowired
    private TableGroupDao tableGroupDao;
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private OrderService orderService;

    @DisplayName("주문 테이블들을 단체 지정할 수 있다.")
    @Test
    void createTableGroup() {
        OrderTable orderTable1 = OrderTable.of(빈_orderTable_id1, 0, true);
        OrderTable orderTable2 = OrderTable.of(빈_orderTable_id2, 0, true);

        TableGroup result = tableGroupService.create(TableGroup.of(1L, Arrays.asList(orderTable1, orderTable2)));

        assertThat(result.getCreatedDate()).isNotNull();
        assertThat(result.getOrderTables().get(0).getTableGroupId()).isEqualTo(1L);
        assertThat(result.getOrderTables().get(0).isEmpty()).isFalse();
        assertThat(result.getOrderTables().get(1).getTableGroupId()).isEqualTo(1L);
        assertThat(result.getOrderTables().get(1).isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블 갯수가 2개 미만일 경우 지정할 수 없다.")
    @Test
    void createTableGroupException1() {
        OrderTable orderTable = OrderTable.of(빈_orderTable_id1, 0, true);

        assertThatThrownBy(() -> tableGroupService.create(TableGroup.of(1L, Collections.singletonList(orderTable))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블들의 수와 저장되어 있는 주문 테이블의 수가 일치하지 않으면 지정할 수 없다.")
    @Test
    void createTableGroupException2() {
        OrderTable orderTable1 = OrderTable.of(빈_orderTable_id1, 0, true);
        OrderTable orderTable2 = OrderTable.of(등록되어_있지_않은_orderTable_id, 0, true);

        assertThatThrownBy(() -> tableGroupService.create(TableGroup.of(1L, Arrays.asList(orderTable1, orderTable2))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 빈 테이블이 아닌 경우 지정할 수 없다.")
    @Test
    void createTableGroupException3() {
        OrderTable orderTable1 = OrderTable.of(빈_orderTable_id1, 0, true);
        OrderTable orderTable2 = OrderTable.of(비어있지_않은_orderTable_id, 0, true);

        assertThatThrownBy(() -> tableGroupService.create(TableGroup.of(1L, Arrays.asList(orderTable1, orderTable2))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 이미 단체 지정되어 있는 경우 지정할 수 없다.")
    @Test
    void createTableGroupException4() {
        OrderTable orderTable1 = OrderTable.of(빈_orderTable_id1, 0, true);
        OrderTable orderTable2 = OrderTable.of(빈_orderTable_id2, 0, true);
        tableGroupService.create(TableGroup.of(1L, Arrays.asList(orderTable1, orderTable2)));

        assertThatThrownBy(() -> tableGroupService.create(TableGroup.of(1L, Arrays.asList(orderTable1, orderTable2))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void ungroupTableGroup() {
        OrderTable orderTable1 = OrderTable.of(빈_orderTable_id1, 0, true);
        OrderTable orderTable2 = OrderTable.of(빈_orderTable_id2, 0, true);
        TableGroup tableGroup = tableGroupService.create(TableGroup.of(1L, Arrays.asList(orderTable1, orderTable2)));

        tableGroupService.ungroup(tableGroup.getId());

        TableGroup result = tableGroupDao.findById(tableGroup.getId()).get();
        assertThat(result.getOrderTables()).isNull();
    }

    @DisplayName("들어간 주문들 중에서 주문 상태가 조리 또는 식사가 하나라도 있을 경우 해제할 수 없다.")
    @Test
    void ungroupTableGroupException() {
        OrderTable orderTable1 = OrderTable.of(빈_orderTable_id1, 0, true);
        OrderTable orderTable2 = OrderTable.of(빈_orderTable_id2, 0, true);
        TableGroup tableGroup = tableGroupService.create(TableGroup.of(1L, Arrays.asList(orderTable1, orderTable2)));

        Order order = Order.of(orderTable1.getId(), Collections.singletonList(OrderLineItem.of(1L, 등록된_menu_id, 2)));
        orderService.create(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
