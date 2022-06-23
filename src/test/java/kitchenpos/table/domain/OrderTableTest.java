package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import kitchenpos.domain.OrderStatus;
import kitchenpos.order.domain.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블 관련 Domain 단위 테스트")
class OrderTableTest {

    @DisplayName("빈 테이블 여부 변경이 가능한지 확인한다.")
    @Test
    void checkPossibleChangeEmpty() {

        //given
        OrderTable orderTable1 = new OrderTable(null, 3, false);
        orderTable1.addOrder(new Order(null, OrderStatus.MEAL, null));
        orderTable1.addOrder(new Order(null, OrderStatus.COMPLETION, null));
        orderTable1.addOrder(new Order(null, OrderStatus.COOKING, null));

        OrderTable orderTable2 = new OrderTable(null, 3, false);
        orderTable2.addOrder(new Order(null, OrderStatus.COMPLETION, null));
        orderTable2.addOrder(new Order(null, OrderStatus.COMPLETION, null));
        orderTable2.addOrder(new Order(null, OrderStatus.COMPLETION, null));

        //when then
        assertThatIllegalStateException()
                .isThrownBy(orderTable1::checkPossibleChangeEmpty);
        assertThatNoException()
                .isThrownBy(orderTable2::checkPossibleChangeEmpty);

    }

    @DisplayName("방문 손님 수가 0명미만으로 업데이트 할 수 없다.")
    @Test
    void updateNumberOfGuests_less_then_one() {

        //given
        OrderTable orderTable = new OrderTable(null, 0, true);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable.updateNumberOfGuests(-1));
    }

    @DisplayName("빈 테이블인 경우 방문 손님 수 업데이트 할 수 없다. ")
    @Test
    void updateNumberOfGuests_not_empty_table() {

        //given
        OrderTable orderTable = new OrderTable(null, 3, true);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable.updateNumberOfGuests(5));
    }

    @DisplayName("단체 지정을 등록한다.")
    @Test
    void assignTableGroup() {
        //given
        OrderTable orderTable = new OrderTable(null, 3, true);

        //when
        orderTable.assignTableGroup(new TableGroup(1L, null));

        // then
        assertThat(orderTable.getTableGroup()).isNotNull();
        assertThat(orderTable.getEmpty()).isFalse();
    }

    @DisplayName("빈 테이블이 아닌 경우 단체지정 할 수 없다.")
    @Test
    void assignTableGroup_not_empty() {
        //given
        OrderTable orderTable = new OrderTable(null, 3, false);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable.assignTableGroup(new TableGroup(2L, null)));
    }

    @DisplayName("이미 단체 지정이 되어있으면 단체지정 할 수 없다.")
    @Test
    void assignTableGroup_already() {
        //given
        OrderTable orderTable = new OrderTable(null, 3, true);
        orderTable.setTableGroup(new TableGroup(1L, null));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable.assignTableGroup(new TableGroup(2L, null)));
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroupingTableGroup() {
        //given
        OrderTable orderTable = new OrderTable(null, 3, true);
        orderTable.assignTableGroup(new TableGroup(1L, null));

        //then
        orderTable.ungroupingTableGroup();

        //then
        assertThat(orderTable.getTableGroup()).isNull();
    }

    @DisplayName("주문 상태가 조리, 식사 인 경우 단체 지정 해제 할 수 없다.")
    @Test
    void ungroupingTableGroup_order_status_cooking_meal() {
        //given
        OrderTable orderTable = new OrderTable(null, 3, true);

        orderTable.assignTableGroup(new TableGroup(1L, null));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(orderTable::ungroupingTableGroup);

    }
}
