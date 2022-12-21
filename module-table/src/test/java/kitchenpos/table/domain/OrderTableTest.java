package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTableTest {

    TableGroup tableGroup;

    @BeforeEach
    void beforeEach(){
        tableGroup = new TableGroup();
    }

    @Test
    @DisplayName("테이블 grouping 테스트")
    void createTest(){
        // given
        OrderTable orderTable1 = new OrderTable(0, true);
        OrderTable orderTable2 = new OrderTable(0, true);

        // when
        orderTable1.grouping(tableGroup);
        orderTable2.grouping(tableGroup);

        // then
        assertThat(orderTable1.isGrouping()).isTrue();
        assertThat(orderTable2.isGrouping()).isTrue();
    }

    @Test
    @DisplayName("테이블 grouping 테스트")
    void groupingTest(){
        // given
        OrderTable orderTable1 = new OrderTable(0, true);
        OrderTable orderTable2 = new OrderTable(0, true);

        // when
        orderTable1.grouping(tableGroup);
        orderTable2.grouping(tableGroup);

        // then
        assertThat(orderTable1.isGrouping()).isTrue();
        assertThat(orderTable2.isGrouping()).isTrue();
    }

    @Test
    @DisplayName("테이블 ungroup 테스트")
    void ungroupTest(){
        // given
        OrderTable orderTable1 = new OrderTable(0, true);
        OrderTable orderTable2 = new OrderTable(0, true);
        orderTable1.grouping(tableGroup);
        orderTable2.grouping(tableGroup);

        // when
        orderTable1.unGroup();
        orderTable2.unGroup();

        // then
        assertThat(orderTable1.isGrouping()).isFalse();
        assertThat(orderTable2.isGrouping()).isFalse();
    }
}
