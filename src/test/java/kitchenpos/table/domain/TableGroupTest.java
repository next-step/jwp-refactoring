package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static kitchenpos.table.domain.TableGroup.ORDER_TABLE_MINIMUM_SIZE_EXCEPTION_MESSAGE;
import static kitchenpos.table.domain.TableGroup.ORDER_TABLE_NOT_EMPTY_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("테이블 그룹")
class TableGroupTest {

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void create() {
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(new OrderTable(1L, new TableGroup(), 1, true));
        orderTables.add(new OrderTable(2L, new TableGroup(), 1, true));
        assertThatNoException().isThrownBy(() -> new TableGroup(orderTables));
    }

    @DisplayName("주문 테이블이 비어있을 수 없다.")
    @Test
    void create_fail_orderTable() {
        List<OrderTable> orderTables = new ArrayList<>();
        assertThatThrownBy(() -> new TableGroup(orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_TABLE_NOT_EMPTY_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문 테이블의 갯수가 2보다 작을 수 없다.")
    @Test
    void name() {
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(new OrderTable());
        assertThatThrownBy(() -> new TableGroup(orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_TABLE_MINIMUM_SIZE_EXCEPTION_MESSAGE);
    }
}
