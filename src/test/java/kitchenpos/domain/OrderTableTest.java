package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import kitchenpos.table.domain.OrderTableEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블에 대한 단위 테스트")
class OrderTableTest {

    @DisplayName("주문 테이블을 테이블 그룹으로 매핑하면 정상적으로 매핑되어야 한다")
    @Test
    void order_table_mapping_test() {
        // given
        Long 테이블_그룹_id = 1L;
        OrderTableEntity orderTable = OrderTableEntity.of(null, 1, true);

        // when
        orderTable.mapIntoGroup(테이블_그룹_id);

        // then
        assertThat(orderTable.getTableGroupId()).isEqualTo(테이블_그룹_id);
    }

    @DisplayName("주문 테이블을 테이블 그룹에서 해제하면 정상적으로 해제되어야 한다")
    @Test
    void order_table_unGroup_test() {
        // given
        OrderTableEntity orderTable = OrderTableEntity.of(1L, 1, true);

        // when
        orderTable.unGroup();

        // then
        assertNull(orderTable.getTableGroupId());
    }
}
