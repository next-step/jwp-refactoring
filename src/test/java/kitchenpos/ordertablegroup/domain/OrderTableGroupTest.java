package kitchenpos.ordertablegroup.domain;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTableGroupTest {

    @DisplayName("주문 테이블 그룹을 생성한다.")
    @Test
    void constructor() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(10, false),
            new OrderTable(20, false)
        );

        // when
        OrderTableGroup orderTableGroup = new OrderTableGroup(new OrderTables(orderTables, orderTables.size()));

        // then
        assertThat(orderTableGroup.getOrderTables().getOrderTables().size()).isEqualTo(orderTables.size());
    }

    @DisplayName("주문 테이블 그룹을 해제한다.")
    @Test
    void unGroup() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(10, false),
            new OrderTable(20, false)
        );
        OrderTableGroup orderTableGroup = new OrderTableGroup(new OrderTables(orderTables, orderTables.size()));

        // when
        orderTableGroup.unGroup();

        // then
        assertThat(orderTables.get(0).getOrderTableGroup()).isNull();
    }

}
