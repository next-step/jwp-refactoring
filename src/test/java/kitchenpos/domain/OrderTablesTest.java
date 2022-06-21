package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.table.domain.OrderTableEntity;
import kitchenpos.table.domain.OrderTables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블 목록에 대한 단위 테스트")
class OrderTablesTest {
    private OrderTableEntity 주문_테이블;
    private OrderTableEntity 주문_테이블2;
    private OrderTables 주문테이블_목록;

    @BeforeEach
    void setUp() {
        주문_테이블 = OrderTableEntity.of(null, 1, true);
        주문_테이블2 = OrderTableEntity.of(null, 1, true);
        주문테이블_목록 = new OrderTables();
    }

    @DisplayName("주문 테이블목록 객체에 주문 테이블을 넣으면 정상적으로 들어간다")
    @Test
    void tables_add_test() {
        // when
        주문테이블_목록.addAll(Arrays.asList(주문_테이블, 주문_테이블2));

        // then
        assertThat(주문테이블_목록.getItems()).hasSize(2);
    }

    @DisplayName("주문 테이블목록에 내의 주문 테이블을 그룹에 매핑하면 정상적으로 매핑된다")
    @Test
    void tables_mapped_test() {
        // given
        주문테이블_목록.addAll(Arrays.asList(주문_테이블, 주문_테이블2));
        Long 테이블_그룹_id = 1L;

        // when
        주문테이블_목록.tablesMapIntoGroup(테이블_그룹_id);

        // then
        for (OrderTableEntity orderTable : 주문테이블_목록.getItems()) {
            assertThat(orderTable.getTableGroupId()).isEqualTo(테이블_그룹_id);
        }
    }

    @DisplayName("주문 테이블목록에 비어있지 않은 객체가 포함되면 예외가 발생한다")
    @Test
    void tables_validate_test() {
        // given
        주문_테이블 = OrderTableEntity.of(null, 1, false);
        주문테이블_목록.addAll(Collections.singletonList(주문_테이블));

        // then
        assertThatThrownBy(주문테이블_목록::validateTablesEmpty)
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블목록에 이미 그룹에 포함된 테이블이 있으면 예외가 발생한다")
    @Test
    void tables_validate_test2() {
        // given
        주문_테이블 = OrderTableEntity.of(1L, 1, true);
        주문테이블_목록.addAll(Collections.singletonList(주문_테이블));

        // then
        assertThatThrownBy(주문테이블_목록::validateTablesEmpty)
            .isInstanceOf(IllegalArgumentException.class);
    }
}
