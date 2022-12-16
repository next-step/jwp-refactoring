package kitchenpos.tablegroup.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.ordertable.domain.OrderTable;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    private OrderTable orderTable = new OrderTable(1, true);
    private OrderTable orderTable2 = new OrderTable(1, true);

    @Test
    void 주문_테이블을_추가할_수_있다() {
        TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable, orderTable2));

        assertThat(tableGroup.getOrderTables()).hasSize(2);
    }

    @Test
    void 빈_테이블이_아닌_주문_테이블이_있을_경우_단체_지정_불가() {
        ThrowingCallable 빈_테이블이_아닌_주문_테이블이_포함_될_경우 = () -> new TableGroup(
                Arrays.asList(orderTable, new OrderTable(1, false)));

        assertThatIllegalArgumentException().isThrownBy(빈_테이블이_아닌_주문_테이블이_포함_될_경우);
    }

    @Test
    void 이미_단체_지정이_된_주문_테이블이_있을_경우_단체_지정_불가() {
        OrderTable 이미_단체_지정이_된_주문_테이블 = new OrderTable(1, true);
        이미_단체_지정이_된_주문_테이블.changeTableGroup(new TableGroup(Arrays.asList(orderTable, orderTable2)));

        ThrowingCallable 이미_단체_지정이_된_주문_테이블이_포함_될_경우 = () -> new TableGroup(
                Arrays.asList(이미_단체_지정이_된_주문_테이블, orderTable2));

        assertThatIllegalArgumentException().isThrownBy(이미_단체_지정이_된_주문_테이블이_포함_될_경우);
    }

    @Test
    void 주문_테이블은_2개이상_지정_해야_한다() {
        ThrowingCallable 주문_테이블은_2개_이하로_지정한_경우 = () -> new TableGroup(Collections.singletonList(orderTable));

        assertThatIllegalArgumentException().isThrownBy(주문_테이블은_2개_이하로_지정한_경우);
    }

    @Test
    void 주문_테이블에_조리_식사_상태가_포함된_주문이_있을경우_해제가_불가능하다() {
        Order order1 = new Order(new OrderTable(1, false), Collections.singletonList(new OrderLineItem(1L, 1)));
        Order order2 = new Order(new OrderTable(1, false), Collections.singletonList(new OrderLineItem(1L, 1)));
        TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable, orderTable2));
        orderTable.addOrder(order1);
        orderTable2.addOrder(order2);

        ThrowingCallable 조리_상태의_주문이_포함된_경우 = tableGroup::unGroup;

        assertThatIllegalArgumentException().isThrownBy(조리_상태의_주문이_포함된_경우);
    }

    @Test
    void 단체_지정을_해제할_수_있다() {
        TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable, orderTable2));
        orderTable.changeTableGroup(tableGroup);
        orderTable2.changeTableGroup(tableGroup);

        tableGroup.unGroup();

        List<TableGroup> tableGroups = tableGroup.getOrderTables().stream()
                .map(OrderTable::getTableGroup)
                .collect(Collectors.toList());
        assertThat(tableGroups).containsExactly(null, null);
    }
}
