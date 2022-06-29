package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    @DisplayName("주문 테이블이 2개보다 작으면 테이블 그룹으로 지정 할 수 없다.")
    void createFailWithEmptyTest() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, false);

        //when & then
        Assertions.assertAll(
                () -> assertThatThrownBy(
                        () -> new TableGroup(1L, Collections.emptyList())
                ).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(
                        () -> new TableGroup(1L,  Arrays.asList(orderTable1))
                ).isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    @DisplayName("이미 테이블 그룹에 속해 있으면 테이블 그룹을 지정 할 수 없다.")
    void createFailWithTableGroupTest() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, 10,true);
        OrderTable orderTable2 = new OrderTable(2L, 20,true);
        TableGroup otherTableGroup = new TableGroup(1L, Arrays.asList(orderTable1, orderTable2));

        //when & then
        assertThatThrownBy(
                () -> new TableGroup(2L, Arrays.asList(orderTable1, orderTable2))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 빈테이블이 아니면 테이블 그룹을 지정 할 수 없다.")
    void createFailWithEmptyTableTest() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, 10,false);
        OrderTable orderTable2 = new OrderTable(2L, 20,false);

        //when & then
        assertThatThrownBy(
                () -> new TableGroup(1L,Arrays.asList(orderTable1, orderTable2))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정을 해지한다.")
    void ungroupTest() {
        //given
        //주문 생성
        OrderLineItem orderLineItem = new OrderLineItem(1L, 10);
        Order order = new Order(1L, orderLineItem);
        // 단체 지정
        OrderTable orderTable1 = new OrderTable(1L, 10,true);
        OrderTable orderTable2 = new OrderTable(2L, 20,true);
        TableGroup tableGroup = new TableGroup(1L,Arrays.asList(orderTable1, orderTable2));
        //주문 상태 변경
        order.changeOrderStatus(OrderStatus.COMPLETION);

        //when & then
        tableGroup.ungroup(Arrays.asList(order));
        assertThat(tableGroup.getOrderTables()).isEmpty();
    }
}
