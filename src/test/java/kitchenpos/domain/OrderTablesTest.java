package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.core.exception.CannotCreateException;
import kitchenpos.core.exception.ExceptionType;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블 목록에 대한 단위 테스트")
class OrderTablesTest {
    private OrderTable 주문_테이블;
    private OrderTable 주문_테이블2;
    private OrderTables 주문테이블_목록;
    private TableGroup 테이블_그룹;

    @BeforeEach
    void setUp() {
        주문_테이블 = OrderTable.of(null, 1, true);
        주문_테이블2 = OrderTable.of(null, 1, true);
    }

    @DisplayName("주문 테이블목록 객체에 주문 테이블을 넣으면 정상적으로 들어간다")
    @Test
    void tables_add_test() {
        // when
        주문테이블_목록 = new OrderTables((Arrays.asList(주문_테이블, 주문_테이블2)), null);

        // then
        assertThat(주문테이블_목록.getItems()).hasSize(2);
    }

    @DisplayName("주문 테이블목록의 주문 테이블을 그룹에 매핑하면 정상적으로 매핑되고 "
        + "테이블 상태가 빈 테이블이 아니게 된다")
    @Test
    void tables_mapped_test() {
        // when
        테이블_그룹 = TableGroup.of(1L, Arrays.asList(주문_테이블, 주문_테이블2));

        // then
        for (OrderTable orderTable : 테이블_그룹.getOrderTables()) {
            assertThat(orderTable.getTableGroup()).isEqualTo(테이블_그룹);
            assertFalse(orderTable.isEmpty());
        }
    }

    @DisplayName("주문 테이블목록의 주문 테이블을 그룹에 해제하면 정상적으로 해제된다")
    @Test
    void tables_unGroup_test() {
        // given
        주문테이블_목록 = new OrderTables((Arrays.asList(주문_테이블, 주문_테이블2)), 테이블_그룹);

        // when
        주문테이블_목록.unGroup();

        // then
        for (OrderTable orderTable : 주문테이블_목록.getItems()) {
            assertNull(orderTable.getTableGroup());
        }
    }

    @DisplayName("주문 테이블목록에 비어있지 않은 객체가 포함되면 예외가 발생한다")
    @Test
    void tables_validate_test() {
        // given
        주문_테이블 = OrderTable.of(null, 1, false);

        // then
        assertThatThrownBy(() -> {
            new OrderTables(Collections.singletonList(주문_테이블), null);
        }).isInstanceOf(CannotCreateException.class)
            .hasMessageContaining(ExceptionType.MUST_NOT_BE_EMPTY_OR_GROUPED_TABLE.getMessage());
    }

    @DisplayName("주문 테이블목록에 이미 그룹에 포함된 테이블이 있으면 예외가 발생한다")
    @Test
    void tables_validate_test2() {
        // given
        테이블_그룹 = TableGroup.of(1L, Arrays.asList(주문_테이블, 주문_테이블2));
        주문_테이블 = OrderTable.of(테이블_그룹, 1, true);

        // then
        assertThatThrownBy(() -> {
            new OrderTables((Collections.singletonList(주문_테이블)), null);
        }).isInstanceOf(CannotCreateException.class)
            .hasMessageContaining(ExceptionType.MUST_NOT_BE_EMPTY_OR_GROUPED_TABLE.getMessage());
    }
}
