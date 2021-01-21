package kitchenpos.table.application;

import kitchenpos.BaseServiceTest;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.utils.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableServiceTest extends BaseServiceTest {
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private TableService tableService;
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private OrderService orderService;

    @DisplayName("주문 테이블을 등록할 수 있다.")
    @Test
    void createTable() {
        OrderTable result = tableService.create(OrderTable.of(등록되어_있지_않은_orderTable_id, 1, false));

        assertThat(result.getId()).isEqualTo(등록되어_있지_않은_orderTable_id);
        assertThat(result.getTableGroupId()).isNull();
    }

    @DisplayName("빈 테이블로 변경할 수 있다.")
    @Test
    void changeEmpty() {
        OrderTable orderTable = OrderTable.of(비어있지_않은_orderTable_id, 0, true);
        OrderTable result = tableService.changeEmpty(orderTable.getId(), orderTable);

        assertThat(result.isEmpty()).isTrue();
    }

    @DisplayName("주문 테이블이 등록되어 있지 않으면 변경할 수 없다.")
    @Test
    void changeEmptyException1() {
        OrderTable orderTable = OrderTable.of(등록되어_있지_않은_orderTable_id, 1, false);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 단체 지정일 경우 변경할 수 없다.")
    @Test
    void changeEmptyException2() {
        OrderTable orderTable1 = orderTableDao.findById(빈_orderTable_id1).get();
        OrderTable orderTable2 = orderTableDao.findById(빈_orderTable_id2).get();
        tableGroupService.create(TableGroup.of(1L, Arrays.asList(orderTable1, orderTable2)));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), orderTable1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문들 중에서 주문 상태가 조리 또는 식사가 하나라도 있을 경우 변경할 수 없다.")
    @Test
    void changeEmptyException3() {
        List<OrderLineItem> orderLineItems = Collections.singletonList(OrderLineItem.of(1L, 등록된_menu_id, 2));
        orderService.create(Order.of(비어있지_않은_orderTable_id, orderLineItems));

        OrderTable orderTable = OrderTable.of(비어있지_않은_orderTable_id, 0, true);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable newOrderTable = OrderTable.of(비어있지_않은_orderTable_id, 3, false);

        OrderTable result = tableService.changeNumberOfGuests(비어있지_않은_orderTable_id, newOrderTable);

        assertThat(result.getNumberOfGuests()).isEqualTo(3);
    }

    @DisplayName("방문한 손님 수가 0보다 작으면 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsException1() {
        OrderTable newOrderTable = OrderTable.of(비어있지_않은_orderTable_id, -1, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(비어있지_않은_orderTable_id, newOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 등록되어 있지 않으면 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsException2() {
        OrderTable newOrderTable = OrderTable.of(등록되어_있지_않은_orderTable_id, 3, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(등록되어_있지_않은_orderTable_id, newOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블일 경우 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsException3() {
        OrderTable newOrderTable = OrderTable.of(빈_orderTable_id1, 4, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(빈_orderTable_id1, newOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
