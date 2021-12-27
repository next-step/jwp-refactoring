package kitchenpos.tablegroup;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.exception.TableGroupNotAvailableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("단체 지정")
class TableGroupTest {


    @Test
    @DisplayName("단체 지정 하고자 하는 테이블의 상태가 사용중이라면 예외가 발생한다.")
    void createTableGroupFailBecauseOfTableNotEmpty() {
        // given
        OrderTable firstOrderTable = new OrderTable(0, false);
        OrderTable secondOrderTable = new OrderTable(0, false);

        // when
        assertThatThrownBy(() -> {
            final TableGroup tableGroup = TableGroup.builder().build();
            tableGroup.saveOrderTable(firstOrderTable);
            tableGroup.saveOrderTable(secondOrderTable);
        }).isInstanceOf(TableGroupNotAvailableException.class);
    }

    @Test
    @DisplayName("단체 지정 하고자 하는 테이블이 단체 지정이 되어 있다면 예외가 발생한다.")
    void createTableGroupFailBecauseOfAlreadyTableGroup() {
        // given
        OrderTable firstOrderTable = new OrderTable(0, true);
        OrderTable secondOrderTable = new OrderTable(0, false);
        final TableGroup existTableGroup = TableGroup.builder().build();
        existTableGroup.saveOrderTable(firstOrderTable);

        // when
        assertThatThrownBy(() -> {
            final TableGroup tableGroup = TableGroup.builder().build();
            tableGroup.saveOrderTable(firstOrderTable);
            tableGroup.saveOrderTable(secondOrderTable);
        }).isInstanceOf(TableGroupNotAvailableException.class);
    }

    @Test
    @DisplayName("단체 지정 해제시 테이블의 주문 상태가 조리 또는 식사 상태면 변경이 불가능하다.")
    void ungroupFailBecauseOfOrderStatusCookingOrMeal() {
        // given
        OrderTable firstOrderTable = new OrderTable(0, true);
        OrderTable secondOrderTable = new OrderTable(0, true);
        firstOrderTable.addOrder(new Order(OrderStatus.COOKING));
        secondOrderTable.addOrder(new Order(OrderStatus.COMPLETION));

        final TableGroup tableGroup = TableGroup.builder().build();
        tableGroup.saveOrderTable(firstOrderTable);
        tableGroup.saveOrderTable(secondOrderTable);


        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableGroup.getOrderTables().forEach(orderTable -> {
                orderTable.ungroup();
            });
        });
    }
}
