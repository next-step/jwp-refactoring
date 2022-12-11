package kitchenpos.domain;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {
    @DisplayName("주문 테이블의 비어있는 여부를 수정할 수 있다.")
    @Test
    void updateEmpty() {
        // given
        boolean expectEmpty = true;
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);
        Order order = new Order(orderTable, OrderStatus.COMPLETION, LocalDateTime.now());

        // when
        orderTable.updateEmpty(expectEmpty, Arrays.asList(order));

        // then
        assertThat(orderTable.isEmpty()).isEqualTo(expectEmpty);
    }

    @DisplayName("주문 테이블의 비어있는 여부를 수정할 때, 단체 지정되어 있다면 수정할 수 없다.")
    @Test
    void updateEmptyGroupException() {
        // given
        OrderTable orderTable1 = new OrderTable(new NumberOfGuests(4), true);
        OrderTable orderTable2 = new OrderTable(new NumberOfGuests(4), true);
        TableGroup tableGroup = new TableGroup(
                LocalDateTime.now(), new OrderTables(Arrays.asList(orderTable1, orderTable2))
        );
        orderTable1.setTableGroup(tableGroup);

        // when & then
        assertThatThrownBy(() -> orderTable1.updateEmpty(true, new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ALREADY_TABLE_GROUP.getMessage());
    }

    @DisplayName("주문 테이블의 비어있는 여부를 수정할 때, 주문의 상태가 계산완료가 아니라면 예외가 발생한다.")
    @Test
    void updateEmptyNotCompletionException() {
        // given
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);
        Order order = new Order(orderTable, OrderStatus.MEAL, LocalDateTime.now());

        // when & then
        assertThatThrownBy(() -> orderTable.updateEmpty(true, Arrays.asList(order)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_STATUS_NOT_COMPLETE.getMessage());
    }

    @DisplayName("주문 테이블의 손님 수를 수정할 수 있다.")
    @Test
    void updateNumberOfGuests() {
        // given
        NumberOfGuests expectNumberOfGuest = new NumberOfGuests(10);
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);

        // when
        orderTable.updateNumberOfGuest(expectNumberOfGuest);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(expectNumberOfGuest.value());
    }

    @DisplayName("주문 테이블의 손님 수를 수정할 때, 주문테이블이 빈 상태라면 예외가 발생한다.")
    @Test
    void updateNumberOfGuestsException() {
        // given
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), true);

        // when & then
        assertThatThrownBy(() -> orderTable.updateNumberOfGuest(new NumberOfGuests(10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_TABLE_IS_EMPTY.getMessage());
    }

    @DisplayName("단체 지정을 해제할 수 있다.")
    @Test
    void ungroup() {
        // given
        OrderTable orderTable1 = new OrderTable(new NumberOfGuests(4), true);
        OrderTable orderTable2 = new OrderTable(new NumberOfGuests(4), true);
        TableGroup tableGroup = new TableGroup(
                LocalDateTime.now(), new OrderTables(Arrays.asList(orderTable1, orderTable2))
        );
        orderTable1.setTableGroup(tableGroup);

        // when
        orderTable1.ungroup();

        // then
        assertThat(orderTable1.getTableGroup()).isNull();
    }
}
