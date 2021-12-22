package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static kitchenpos.table.domain.OrderTableTest.빈자리;
import static org.assertj.core.api.Assertions.assertThat;

public class TableGroupTest {
    public static final TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now());

    @Test
    @DisplayName("테이블 그룹 생성")
    public void create() {
        // given
        // when
        TableGroup actual = new TableGroup(1L, LocalDateTime.now());
        // then
        assertThat(actual).isEqualTo(테이블그룹);
    }

    @Test
    @DisplayName("단체 지정 해제")
    public void ungroupTest() {
        // given
        OrderTable 단체자리 = new OrderTable(4, true);
        OrderTables orderTables = new OrderTables(Arrays.asList(단체자리, 단체자리));
        TableGroup 단체테이블 = new TableGroup(orderTables);
        // when
        단체테이블.ungroup();
        // then
        assertThat(단체테이블.getOrderTables()).hasSize(0);
    }
}