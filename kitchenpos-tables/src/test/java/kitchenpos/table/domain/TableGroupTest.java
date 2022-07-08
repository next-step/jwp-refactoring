package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단체 지정")
class TableGroupTest {
    @DisplayName("단체 지정할 수 있다.")
    @Test
    void 단체_지정() {
        OrderTable 주문_테이블1 = OrderTable.of(2, false);
        OrderTable 주문_테이블2 = OrderTable.of(3, false);

        OrderTables 주문_테이블들 = new OrderTables(Arrays.asList(주문_테이블1, 주문_테이블2));

        TableGroup 단체_지정 = TableGroup.of(1L, OrderTables.create());
        단체_지정.group(주문_테이블들);

        assertThat(주문_테이블들.value()).allSatisfy(주문_테이블 -> assertThat(주문_테이블.getTableGroupId()).isEqualTo(1L));
    }

    @DisplayName("단체 지정을 삭제할 수 있다.")
    @Test
    void 단체_지정_삭제() {
        OrderTable 주문_테이블1 = new OrderTable(1L, 3, false);
        OrderTable 주문_테이블2 = new OrderTable(2L, 5, false);
        OrderTables 주문_테이블들 = new OrderTables(Arrays.asList(주문_테이블1, 주문_테이블2));

        TableGroup 단체_지정 = TableGroup.of(1L, OrderTables.create());
        단체_지정.group(주문_테이블들);

        단체_지정.ungroup();

        assertThat(주문_테이블들.value()).allSatisfy(주문_테이블 -> assertThat(주문_테이블.getTableGroupId()).isNull());
    }
}
