package kitchenpos.tablegroup;

import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TableGroupTest {

    @DisplayName("주문 테이블 단체 지정하기")
    @Test
    void groupingTable() {

        //given

        //when
        TableGroup tableGroup = TableGroup.setUp();

        //then
        assertThat(tableGroup).isNotNull();
    }

    @DisplayName("단체 지정 해제하기")
    @Test
    void unGroup() {

        //given
        final long tableGroupId = 1L;
        OrderTable orderTableA = OrderTable.setting(10, false);
        ReflectionTestUtils.setField(orderTableA, "id", 1L);

        orderTableA.grouping(tableGroupId);
        Order orderA = orderTableA.placeOrder();
        orderA.completion();

        OrderTable orderTableB = OrderTable.setting(7, false);
        ReflectionTestUtils.setField(orderTableB, "id", 2L);
        orderTableB.grouping(tableGroupId);
        Order orderB = orderTableB.placeOrder();
        orderB.completion();

        //when
        orderTableA.unGrouping();
        orderTableB.unGrouping();

        //then
        assertThat(orderTableA.getTableGroupId()).isNull();
        assertThat(orderTableB.getTableGroupId()).isNull();
    }

    @DisplayName("단체 지정 해제 시 요리중인 주문이 있거나 식사중인 주문이 있을경우")
    @Test
    void unGroupByOrderStatusCookingAndMeal() {

        //given
        final long tableGroupId = 1L;
        TableGroup tableGroup = TableGroup.setUp();

        OrderTable orderTableA = OrderTable.setting(10, false);
        ReflectionTestUtils.setField(orderTableA, "id", 1L);
        orderTableA.grouping(tableGroupId);
        orderTableA.placeOrder();

        OrderTable orderTableB = OrderTable.setting(7, false);
        ReflectionTestUtils.setField(orderTableB, "id", 2L);
        orderTableB.grouping(tableGroupId);

        //when
        assertThatThrownBy(() -> {
            orderTableA.unGrouping();
            orderTableB.unGrouping();
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
