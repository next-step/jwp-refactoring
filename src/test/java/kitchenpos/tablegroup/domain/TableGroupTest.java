package kitchenpos.tablegroup.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("TableGroup 클래스 테스트")
class TableGroupTest {

    private final List<OrderTable> orderTables = Arrays.asList(new OrderTable(0, true),
                                                                     new OrderTable(0, true),
                                                                     new OrderTable(0, true));
    @DisplayName("단체 지정을 생성한다.")
    @Test
    void create() {
        TableGroup tableGroup = new TableGroup();
        assertThat(tableGroup.getCreatedDate()).isNotNull();
    }

    @DisplayName("단체 지정에 테이블들을 추가한다.")
    @Test
    void addOrderTables() {
        TableGroup tableGroup = new TableGroup();

        tableGroup.addOrderTables(orderTables);

        assertThat(tableGroup.getOrderTables()).hasSize(3);
    }

    @DisplayName("1개의 테이블을 단체 지정에 추가한다.")
    @Test
    void addOrderTablesWithSizeIsOne() {
        TableGroup tableGroup = new TableGroup();

        assertThatThrownBy(() -> {
            tableGroup.addOrderTables(Arrays.asList(new OrderTable(0, true)));
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("테이블 갯수가 적습니다.");
    }

    @DisplayName("빈 테이블이 포함된 테이블들을 단체 지정에 추가한다.")
    @Test
    void addOrderTablesWithHasNotEmpty() {
        TableGroup tableGroup = new TableGroup();

        assertThatThrownBy(() -> {
            tableGroup.addOrderTables(Arrays.asList(new OrderTable(0, false),
                                                    new OrderTable(0, true)));
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("테이블 중에 빈 테이블이 아니거나 다른 단체에 지정된 테이블이 있습니다.");
    }

    @DisplayName("다른 단체 지정된 테이블이 포함된 테이블들을 단체 지정에 추가한다.")
    @Test
    void addOrderTablesWithHasGroup() {
        TableGroup tableGroup = new TableGroup();
        TableGroup other = new TableGroup();
        OrderTable orderTable = new OrderTable(0, true);
        orderTable.bindTo(other);

        assertThatThrownBy(() -> {
            tableGroup.addOrderTables(Arrays.asList(orderTable,
                                                    new OrderTable(0, true)));
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("테이블 중에 빈 테이블이 아니거나 다른 단체에 지정된 테이블이 있습니다.");
    }

    @DisplayName("단체 지정을 해지한다.")
    @Test
    void ungroup() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.addOrderTables(orderTables);

        tableGroup.ungroup();

        assertThat(tableGroup.getOrderTables()).isEmpty();
    }
}
