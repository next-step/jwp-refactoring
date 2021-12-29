package kitchenpos.table;

import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTableTest {
    @Test
    @DisplayName("주문 테이블을 생성하고 그룹을 지정한다.")
    void createOrderTableAndGroup() {
        OrderTable 주문테이블 = new OrderTable(4, true);
        주문테이블.group(1L);
        assertThat(주문테이블.getTableGroupId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("주문 테이블이 비어있지 않으면 단체 지정에 실패한다.")
    void groupOfNotEmptyOrderTable() {
        OrderTable 주문테이블 = new OrderTable(4, false);
        assertThatThrownBy(() -> 주문테이블.group(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비어있지 않거나");
    }

    @Test
    @DisplayName("단체가 지정되어 있으면 다시 지정할 수 없다.")
    void groupOfAlreadyGroupedTable() {
        OrderTable 주문테이블 = new OrderTable(4, true);
        주문테이블.group(1L);

        assertThatThrownBy(() -> 주문테이블.group(2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("단체 지정된 테이블");
    }

    @Test
    @DisplayName("단체 지정을 해제한다.")
    void ungroup() {
        OrderTable 주문테이블 = new OrderTable(4, true);
        주문테이블.group(1L);
        주문테이블.ungroup();
        assertThat(주문테이블.getTableGroupId()).isNull();
    }

}
