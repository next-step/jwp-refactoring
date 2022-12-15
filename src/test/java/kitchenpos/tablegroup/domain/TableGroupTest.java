package kitchenpos.tablegroup.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class TableGroupTest {
//    @DisplayName("단체 지정된 테이블을 해제할 수 있다.")
//    @Test
//    void unGroup() {
//        // given
//        Order order1 = TestOrderFactory.createCompleteOrder();
//        Order order2 = TestOrderFactory.createCompleteOrder();
//
//        OrderTable orderTable1 = order1.getOrderTable();
//        OrderTable orderTable2 = order2.getOrderTable();
//
//        TableGroup tableGroup = new TableGroup(
//                LocalDateTime.now(),
//                new OrderTables(Arrays.asList(orderTable1, orderTable2))
//        );
//
//        orderTable1.setTableGroup(tableGroup);
//        orderTable2.setTableGroup(tableGroup);
//
//        // when
//        tableGroup.ungroup(Arrays.asList(order1, order2));
//
//        // then
//        assertAll(
//                () -> assertThat(orderTable1.getTableGroup()).isNull(),
//                () -> assertThat(orderTable2.getTableGroup()).isNull()
//        );
//    }
//
//    @DisplayName("단체 지정된 테이블을 해제할 때, 주문상태가 결제완료가 아니라면 예외가 발생한다.")
//    @Test
//    void unGroupException() {
//        // given
//        Order order1 = TestOrderFactory.createCompleteOrder();
//        Order order2 = TestOrderFactory.createCompleteOrder();
//
//        TableGroup tableGroup = new TableGroup(
//                LocalDateTime.now(),
//                new OrderTables(Arrays.asList(order1.getOrderTable(), order1.getOrderTable()))
//        );
//
//        order1.setOrderStatus(OrderStatus.MEAL);
//
//        // when & then
//        assertThatThrownBy(() -> tableGroup.ungroup(Arrays.asList(order1, order2)))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining(ErrorCode.ORDER_STATUS_NOT_COMPLETE.getMessage());
//    }
}
