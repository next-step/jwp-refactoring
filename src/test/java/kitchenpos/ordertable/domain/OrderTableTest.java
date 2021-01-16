package kitchenpos.ordertable.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.tablegroup.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class OrderTableTest {

    @Test
    @DisplayName("주문 테이블을 등록할 수 있다.")
    public void create() throws Exception {
        // when
        OrderTable orderTable = new OrderTable(0, true);

        // then
        assertThat(orderTable).isNotNull();
    }

    @Test
    @DisplayName("빈 테이블 설정 또는 해지할 수 있다.")
    public void changeEmpty() throws Exception {
        // given
        OrderTable orderTable = new OrderTable(0, true);

        // when
        orderTable.changeEmpty(false);

        // then
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("단체 지정된 주문 테이블은 빈 테이블 설정 또는 해지할 수 없다.")
    public void changeEmptyFail1() throws Exception {
        // given
        OrderTable orderTable1 = new OrderTable(3, true);
        OrderTable orderTable2 = new OrderTable(5, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        new TableGroup(new OrderTables(orderTables));

        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable1.changeEmpty(true))
                .withMessageMatching("단체 지정된 주문 테이블은 빈 테이블 설정 또는 해지할 수 없습니다.");
    }

    @Test
    @DisplayName("주문 상태가 조리 또는 식사인 주문 테이블은 빈 테이블 설정 또는 해지할 수 없다.")
    public void changeEmptyFail2() throws Exception {
        // given
        OrderTable orderTable = new OrderTable(5, false);
        List<OrderLineItem> orderLineItems = Collections.singletonList(new OrderLineItem(null, 1));
        Order.of(orderTable, new OrderLineItems(orderLineItems));

        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable.changeEmpty(true))
                .withMessageMatching("조리 또는 식사인 테이블은 빈 테이블 설정 또는 해지할 수 없습니다.");
    }

    @Test
    @DisplayName("방문한 손님 수를 입력할 수 있다.")
    public void changeNumberOfGuests() throws Exception {
        // given
        OrderTable orderTable = new OrderTable(0, false);

        // when
        orderTable.changeNumberOfGuests(5);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    @DisplayName("방문한 손님 수가 올바르지 않으면 입력할 수 없다. : 방문한 손님 수는 0 명 이상이어야 한다.")
    public void changeNumberOfGuestsFail() throws Exception {
        // given
        OrderTable orderTable = new OrderTable(0, false);

        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .withMessageMatching("방문한 손님 수는 0명 이상이어야 합니다.");
    }

    @Test
    @DisplayName("빈 테이블은 방문한 손님 수를 입력할 수 없다.")
    public void notInputEmptyTable() throws Exception {
        // given
        OrderTable orderTable = new OrderTable(0, true);
        orderTable.changeEmpty(true);

        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable.changeNumberOfGuests(5))
                .withMessageMatching("빈 테이블은 방문한 손님 수를 입력할 수 없습니다.");
    }
}
