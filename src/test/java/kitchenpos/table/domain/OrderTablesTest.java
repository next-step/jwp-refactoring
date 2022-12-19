package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTablesTest {

    @Test
    @DisplayName("주문 테이블 목록 생성에 성공한다")
    void createOrderTablesTest() {
        // given
        List<OrderTable> orderTableItems = Arrays.asList(
                OrderTable.of(1, true),
                OrderTable.of(2, true),
                OrderTable.of(3, true)
        );

        // when
        OrderTables orderTables = new OrderTables(orderTableItems);

        // then
        assertThat(orderTables).isEqualTo(new OrderTables(orderTableItems));
    }

    @Test
    @DisplayName("테이블 그룹 지정에 성공한다")
    void groupOrderTablesTest() {
        // given
        List<OrderTable> orderTableItems = Arrays.asList(
                OrderTable.of(1, true),
                OrderTable.of(2, true),
                OrderTable.of(3, true)
        );
        OrderTables orderTables = new OrderTables(orderTableItems);

        // when
        orderTables.groupBy(1L);

        // then
        boolean groupByGivenTableGroup = orderTables.getAll()
                .stream()
                .allMatch(OrderTable::isGrouped);
        assertThat(groupByGivenTableGroup).isTrue();
    }

    @Test
    @DisplayName("테이블 그룹 해지에 성공한다")
    void unGroupOrderTablesTest() {
        // given
        List<OrderTable> orderTableItems = Arrays.asList(
                OrderTable.of(1, true),
                OrderTable.of(2, true),
                OrderTable.of(3, true)
        );
        OrderTables orderTables = new OrderTables(orderTableItems);
        orderTables.groupBy(1L);

        // when
        orderTables.unGroup();

        // then
        boolean unGroupByGivenTableGroup = orderTables.getAll()
                .stream()
                .noneMatch(OrderTable::isGrouped);
        assertThat(unGroupByGivenTableGroup).isTrue();
    }
}
