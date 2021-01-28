package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class TableGroupTest {

    @Test
    @DisplayName("단체지정 등록시 예외사항")
    void getOrderTableIds() {
        TableGroup tableGroup = new TableGroup();

        assertThatThrownBy(() -> {
            tableGroup.getOrderTableIds(null);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("저장된 주문테이블 비교시 예외사항(개수 안 맞음)")
    void compareOrderTables1() {
        List<OrderTable> orderTables = new ArrayList<>();
        List<OrderTable> savedTables = new ArrayList<>();
        orderTables.add(new OrderTable());
        orderTables.add(new OrderTable());
        savedTables.add(new OrderTable());

        TableGroup tableGroup = new TableGroup();

        assertThatThrownBy(() -> {
            tableGroup.compareOrderTables(orderTables, savedTables);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("저장된 주문테이블 비교시 예외사항(빈테이블 아닐경우")
    void compareOrderTables2() {
        List<OrderTable> orderTables = new ArrayList<>();
        List<OrderTable> savedTables = new ArrayList<>();
        orderTables.add(new OrderTable(1L, null, 4, false));
        savedTables.add(new OrderTable(1L, null, 4, false));

        TableGroup tableGroup = new TableGroup();

        assertThatThrownBy(() -> {
            tableGroup.compareOrderTables(orderTables, savedTables);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
