package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.OrderException;
import kitchenpos.table.exception.OrderTablesException;
import kitchenpos.table.exception.OrderTablesExceptionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TableGroupTest {

    @DisplayName("테이블그룹에 테이블을 추가할경우 테이블이 빈값일경우 예외발생")
    @Test
    public void throwsExceptionWhenOrderTableIsEmpty() {
        TableGroup tableGroup = TableGroup.builder().build();

        assertThatThrownBy(() -> tableGroup.addOrderTables(Collections.EMPTY_LIST))
                .isInstanceOf(OrderTablesException.class)
                .hasMessageContaining("추가할 테이블이 없습니다");
    }

    @DisplayName("테이블그룹에 테이블을 추가할경우 테이블이 없는경우 예외발생")
    @Test
    public void throwsExceptionWhenNullTable() {
        TableGroup tableGroup = TableGroup.builder().build();

        assertThatThrownBy(() -> tableGroup.addOrderTables(null))
                .isInstanceOf(OrderTablesException.class)
                .hasMessageContaining("추가할 테이블이 없습니다");
    }

    @DisplayName("테이블그룹에 테이블을 추가할경우 추가할 테이블이 2개 미만인경우 예외발생")
    @Test
    public void throwsExceptionWhenLessThenMinSize() {
        TableGroup tableGroup = TableGroup
                .builder()
                .orderTables(OrderTables.of(Arrays.asList(OrderTable.builder().build())))
                .build();

        assertThatThrownBy(() -> tableGroup.addOrderTables(Arrays.asList(OrderTable.builder().build())))
                .isInstanceOf(OrderTablesException.class)
                .hasMessageContaining("테이블 개수가 부족합니다");

    }

    @DisplayName("테이블그룹에 테이블을 추가할경우 추가할 테이블이 공석이 아니면 예외발생")
    @Test
    public void throwsExceptionWhenNegativeGuest() {
        List<OrderTable> orderTables = Arrays.asList(OrderTable.builder().build(), OrderTable.builder().build());
        TableGroup tableGroup = TableGroup
                .builder()
                .orderTables(OrderTables.of(Arrays.asList(OrderTable.builder().build())))
                .build();

        assertThatThrownBy(() -> tableGroup.addOrderTables(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블그룹에 테이블을 추가할경우 추가할 테이블에 이미 그룹이 있는경우 예외발생")
    @Test
    public void throwsExceptionWhenEmptyTable() {
        OrderTable orderTable1 = OrderTable.builder().tableGroup(TableGroup.builder().build()).build();
        OrderTable orderTable2 = OrderTable.builder().build();
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroup tableGroup = TableGroup
                .builder()
                .orderTables(OrderTables.of(Arrays.asList(OrderTable.builder().build())))
                .build();

        assertThatThrownBy(() -> tableGroup.addOrderTables(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블그룹에 테이블을 해제할경우 주문이 식사중이면 예외발생")
    @Test
    public void throwsExceptionWhenOderIsMeal() {
        List<Order> orders = Arrays.asList(Order.builder().orderTable(OrderTable.builder().build()).orderStatus(OrderStatus.MEAL).build());
        TableGroup tableGroup = TableGroup
                .builder()
                .orderTables(OrderTables.of(Arrays.asList(OrderTable.builder().build())))
                .build();

        assertThatThrownBy(() -> tableGroup.ungroup(orders))
                .isInstanceOf(OrderException.class)
                .hasMessageContaining("계산이 끝나지 않은 주문은 상태를 변경할 수 없습니다");
    }

    @DisplayName("테이블그룹에 테이블을 해제할경우 주문이 조리중이면 예외발생")
    @Test
    public void throwsExceptionWhenOderIsCooking() {
        List<Order> orders = Arrays.asList(Order.builder().orderTable(OrderTable.builder().build()).orderStatus(OrderStatus.COOKING).build());
        TableGroup tableGroup = TableGroup
                .builder()
                .orderTables(OrderTables.of(Arrays.asList(OrderTable.builder().build())))
                .build();

        assertThatThrownBy(() -> tableGroup.ungroup(orders))
                .isInstanceOf(OrderException.class)
                .hasMessageContaining("계산이 끝나지 않은 주문은 상태를 변경할 수 없습니다");
    }

}
