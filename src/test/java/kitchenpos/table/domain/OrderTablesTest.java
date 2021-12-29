package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.exception.KitchenposException;
import kitchenpos.tablegroup.domain.GroupingTableEvent;

class OrderTablesTest {

    @DisplayName("주문테이블들 그룹화")
    @Test
    void group() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, null, 4, true),
            new OrderTable(2L, null, 4, true)
        );

        OrderTables tables = new OrderTables(orderTables);
        GroupingTableEvent event = new GroupingTableEvent(1L, Arrays.asList(1L, 2L));

        // when
        tables.group(event);

        // then
        assertAll(
            () -> assertThat(tables.getOrderTables().get(0).getTableGroupId()).isEqualTo(1),
            () -> assertThat(tables.getOrderTables().get(1).getTableGroupId()).isEqualTo(1)
        );
    }

    @DisplayName("사용중인 주문테이블이 있으면 에러")
    @Test
    void checkNotContainsUsedTable() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, null, 4, false),
            new OrderTable(2L, null, 4, false)
        );

        OrderTables tables = new OrderTables(orderTables);

        // when and then
        GroupingTableEvent event = new GroupingTableEvent(1L, Arrays.asList(1L, 2L));
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> tables.group(event))
            .withMessage("사용중인 테이블이 있습니다.");
    }

    @DisplayName("입력받은 주문 테이블 개수와 실제 주문 테이블 개수가 다른 경우(메뉴에 없는 주문 테이블) 생성 불가능")
    @Test
    void createTableGroupFailWhenTableNumberIsDifferent() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, null, 4, false),
            new OrderTable(2L, null, 4, false)
        );

        OrderTables tables = new OrderTables(orderTables);

        GroupingTableEvent event = new GroupingTableEvent(1L, Arrays.asList(1L, 2L, 3L));

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> tables.group(event))
            .withMessage("주문 테이블의 개수가 다릅니다.");
    }

    @Test
    void unGroup() {
        List<OrderTable> orderTables = Collections.singletonList(
            new OrderTable(1L, 1L, 4, true));

        OrderTables tables = new OrderTables(orderTables);
        tables.unGroup();

        assertThat(tables.getOrderTables().get(0).getTableGroupId()).isNull();
    }
}