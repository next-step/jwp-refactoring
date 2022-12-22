package kitchenpos.table.domain;

import kitchenpos.table.message.OrderTableMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    @DisplayName("테이블 그룹 지정시 주문 테이블의 개수가 2개 미만인 경우 생성에 실패한다")
    void groupOrderTablesThrownByLessThanTwoTablesTest() {
        // given
        OrderTables orderTables = new OrderTables(Arrays.asList(
                OrderTable.of(1, true)
        ));

        // when & then
        assertThatThrownBy(() -> orderTables.groupBy(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableMessage.GROUP_ERROR_MORE_THAN_TWO_ORDER_TABLES.message());
    }

    @Test
    @DisplayName("테이블 그룹 지정시 다른 테이블 그룹에 등록 된 주문 테이블이 주어진 경우 생성에 실패한다")
    void groupOrderTablesThrownByEnrolledOtherGroupTest() {
        // given
        OrderTable orderTable = OrderTable.of(1, true);
        OrderTables otherOrderTables = new OrderTables(Arrays.asList(
                orderTable,
                OrderTable.of(2, true)
        ));
        otherOrderTables.groupBy(1L);

        OrderTables orderTables = new OrderTables(Arrays.asList(
                orderTable,
                OrderTable.of(2, true)
        ));

        // when & then
        assertThatThrownBy(() -> orderTables.groupBy(2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableMessage.GROUP_ERROR_OTHER_TABLE_GROUP_MUST_BE_NOT_ENROLLED.message());
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
