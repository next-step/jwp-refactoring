package kitchenpos.ordertable.domain;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.fixture.TestOrderFactory;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
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
        Order order = TestOrderFactory.create(orderTable.getId(), OrderStatus.COMPLETION, new ArrayList<>());

        // when
        orderTable.setEmpty(expectEmpty);

        // then
        assertThat(orderTable.isEmpty()).isEqualTo(expectEmpty);
    }

    @DisplayName("주문 테이블의 손님 수를 수정할 수 있다.")
    @Test
    void updateNumberOfGuests() {
        // given
        NumberOfGuests expectNumberOfGuest = new NumberOfGuests(10);
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);

        // when
        orderTable.setNumberOfGuest(expectNumberOfGuest);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(expectNumberOfGuest.value());
    }

    @DisplayName("단체 지정을 해제할 수 있다.")
    @Test
    void ungroup() {
        // given
        OrderTable orderTable1 = new OrderTable(new NumberOfGuests(4), true);
        OrderTable orderTable2 = new OrderTable(new NumberOfGuests(4), true);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());
        orderTable1.setTableGroupId(tableGroup.getId());

        // when
        orderTable1.ungroup();

        // then
        assertThat(orderTable1.getTableGroupId()).isNull();
    }
}
