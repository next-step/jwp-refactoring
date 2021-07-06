package kitchenpos.tablegroup.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;

public class TableGroupTest {

    private List<OrderTable> orderTables;

    @BeforeEach
    void setUp() {
        //given
        orderTables = new ArrayList<>();
        orderTables.add(new OrderTable(1L, 10, true));
        orderTables.add(new OrderTable(1L, 10, true));
    }

    @Test
    @DisplayName("TableGroup 정상적으로 객체가 생성된다.")
    void make() {

        //when
        TableGroup tableGroup = TableGroup.make(Arrays.asList(1L, 2L), new OrderTables(orderTables));

        //then
        assertThat(tableGroup.getOrderTables()).containsExactly(orderTables.get(0), orderTables.get(1));
    }

    @Test
    @DisplayName("주문_테이블은 1개 주어질 때 에러를 뱉는다")
    void oneOrder() {
        //then
        assertThrows(IllegalArgumentException.class, () -> {
            TableGroup.make(Arrays.asList(1L), new OrderTables(orderTables));
        });
    }

    @Test
    @DisplayName("중복된 주문이나, 존재하지 않는 주문이 입력으로 들어오면 에러를 뱉는다")
    void same() {

        //given
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(new OrderTable(1L, 10, true));

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            TableGroup.make(Arrays.asList(1L, 2L), new OrderTables(orderTables));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            TableGroup.make(Collections.emptyList(), new OrderTables(orderTables));
        });
    }

    @Test
    @DisplayName("주문 테이블에 그룹이 존재하면 에러를 뱉는다")
    void alreadyGroup() {

        //given
        List<OrderTable> orderTables = new ArrayList<>();
        //given
        orderTables.add(new OrderTable(1L, new TableGroup(1L, LocalDateTime.now()), 10, true));
        orderTables.add(new OrderTable(1L, new TableGroup(1L, LocalDateTime.now()), 10, true));

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            TableGroup.make(Arrays.asList(1L, 2L), new OrderTables(orderTables));
        });
    }

    @Test
    @DisplayName("빈 테이블이 아닐 시 에러를 뱉는다")
    void fullTable() {

        //given
        List<OrderTable> orderTables = new ArrayList<>();
        //given
        orderTables.add(new OrderTable(1L, 10, false));
        orderTables.add(new OrderTable(1L, 10, true));

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            TableGroup.make(Arrays.asList(1L, 2L), new OrderTables(orderTables));
        });
    }
}
