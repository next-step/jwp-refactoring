package kitchenpos.table;

import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TableGroupTest {


    @DisplayName("주문 테이블 단체 지정하기")
    @Test
    void groupingTable() {

        //given

        //when
        TableGroup tableGroup = TableGroup.create();

        //then
        assertThat(tableGroup).isNotNull();
    }

    @DisplayName("주문 테이블 추출")
    @Test
    void findOrderTable() {

        //given
        TableGroup tableGroup = TableGroup.create();
        OrderTable orderTableA = OrderTable.create(10, true);
        ReflectionTestUtils.setField(orderTableA, "id", 1L);

        OrderTable orderTableB = OrderTable.create(7, true);
        ReflectionTestUtils.setField(orderTableB, "id", 2L);

        tableGroup.addOrderTable(orderTableA);
        tableGroup.addOrderTable(orderTableB);

        //when
        List<OrderTable> orderTables = tableGroup.findOrderTables();

        //then
        assertThat(orderTables).contains(orderTableA, orderTableB);
    }

    @DisplayName("주문 테이블 식별자 추출")
    @Test
    void findOrderTableIds() {

        //given
        TableGroup tableGroup = TableGroup.create();
        OrderTable orderTableA = OrderTable.create(10, true);
        ReflectionTestUtils.setField(orderTableA, "id", 1L);

        OrderTable orderTableB = OrderTable.create(7, true);
        ReflectionTestUtils.setField(orderTableB, "id", 2L);

        tableGroup.addOrderTable(orderTableA);
        tableGroup.addOrderTable(orderTableB);

        //when
        List<Long> ids = tableGroup.findOrderTableIds();

        //then
        assertThat(ids).contains(1L, 2L);
    }

    @DisplayName("단체 지정 해제하기")
    @Test
    void unGroup() {

        //given
        TableGroup tableGroup = TableGroup.create();
        OrderTable orderTableA = OrderTable.create(10, true);
        ReflectionTestUtils.setField(orderTableA, "id", 1L);
        Order orderA = new Order();
        orderA.completion();
        orderTableA.order(orderA);

        OrderTable orderTableB = OrderTable.create(7, true);
        Order orderB = new Order();
        orderB.completion();
        orderTableB.order(orderB);
        ReflectionTestUtils.setField(orderTableB, "id", 2L);

        tableGroup.addOrderTable(orderTableA);
        tableGroup.addOrderTable(orderTableB);

        //when
        tableGroup.unGrouping();

        //then
        assertThat(tableGroup.findOrderTables().size()).isEqualTo(0);
    }

    @DisplayName("단체 지정 해제 시 요리중인 주문이 있거나 식사중인 주문이 있을경우")
    @Test
    void unGroupByOrderStatusCookingAndMeal() {

        //given
        TableGroup tableGroup = TableGroup.create();
        OrderTable orderTableA = OrderTable.create(10, true);
        ReflectionTestUtils.setField(orderTableA, "id", 1L);
        Order orderA = new Order();
        orderA.cooking();
        orderTableA.order(orderA);

        OrderTable orderTableB = OrderTable.create(7, true);
        Order orderB = new Order();
        orderB.completion();
        orderTableB.order(orderB);
        ReflectionTestUtils.setField(orderTableB, "id", 2L);

        tableGroup.addOrderTable(orderTableA);
        tableGroup.addOrderTable(orderTableB);

        //when
        assertThatThrownBy(() -> tableGroup.unGrouping()).isInstanceOf(IllegalArgumentException.class);
    }
}
