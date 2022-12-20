package kitchenpos.table.domain;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("테이블그룹 테스트")
class TableGroupTest {

    @Test
    @DisplayName("비어있지않은 테이블이 포함된 경우 테이블 그룹 지정 실패")
    void update_error_exist_table_group() {
        OrderTable emptyTable = new OrderTable(0, true);
        OrderTable notEmptyTable = new OrderTable(0, false);

        List<OrderTable> orderTables = Lists.newArrayList(emptyTable, notEmptyTable);


        assertThatThrownBy(() -> new TableGroup(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 리스트가 없거나 테이블이 2개 미만이면 테이블 그룹을 등록할 수 없다.")
    void create_error_order_table_less_then_2() {
        int numberOfGuests = 0;
        OrderTable emptyTable = new OrderTable(numberOfGuests, true);

        List<OrderTable> orderTables = Lists.newArrayList(emptyTable);
        assertThatThrownBy(() -> new TableGroup(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
