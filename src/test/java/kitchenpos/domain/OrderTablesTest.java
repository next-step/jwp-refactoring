package kitchenpos.domain;


import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OrderTablesTest {
    @DisplayName("OrderTables를 생성한다")
    @Test
    void testCreate() {
        // given
        List<OrderTable> orderTables = Arrays.asList(new OrderTable(), new OrderTable(), new OrderTable());

        // when
        OrderTables result = new OrderTables(orderTables);

        // then
        assertThat(result.values()).isEqualTo(orderTables);
    }

    @DisplayName("기대한 숫자와 동일한 갯수의 리스트를 생성한다")
    @Test
    void testExpectedSize() {
        // given
        List<OrderTable> orderTables = Arrays.asList(new OrderTable(4, true), new OrderTable(4, true), new OrderTable(4, true));

        // when
        OrderTables result = OrderTables.of(new TableGroup(), orderTables, orderTables.size());

        // then
        assertThat(result.values()).isEqualTo(orderTables);
    }

    @DisplayName("기대한 숫자와 동일한 갯수의 리스트를 생성해야 한다")
    @Test
    void testGivenDifferentExpectedSize() {
        // given
        List<OrderTable> orderTables = Arrays.asList(new OrderTable(4, true), new OrderTable(4, true), new OrderTable(4, true));

        // when
        ThrowableAssert.ThrowingCallable callable = () -> OrderTables.of(new TableGroup(), orderTables, orderTables.size() + 1);

        // then
        assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제한다")
    @Test
    void testUngroup() {
        // given
        List<OrderTable> orderTableList = Arrays.asList(new OrderTable(), new OrderTable(), new OrderTable());
        OrderTables orderTables = new OrderTables(orderTableList);

        // when
        orderTables.ungroup();

        // then
        assertAll(
                () -> assertThat(orderTables.isEmpty()).isTrue(),
                () -> assertThat(orderTableList)
                        .map(OrderTable::getTableGroup)
                        .allMatch(Objects::isNull)
        );
    }

    @DisplayName("주문이 생성되지 않아야 단체 지정을 해제할 수 있다")
    @Test
    void testHasNotOrder() {
        // given
        TableGroup tableGroup = new TableGroup();
        List<Order> orders = Arrays.asList(new Order(OrderStatus.COOKING), new Order(OrderStatus.COOKING));
        OrderTable one = new OrderTable(1L, tableGroup, 4, false, orders);
        OrderTable two = new OrderTable(2L, tableGroup, 4, false, orders);
        List<OrderTable> orderTableList = Arrays.asList(one, two);
        OrderTables orderTables = new OrderTables(orderTableList);

        // when
        ThrowableAssert.ThrowingCallable callable = () -> orderTables.ungroup();

        // then
        assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
    }
}
