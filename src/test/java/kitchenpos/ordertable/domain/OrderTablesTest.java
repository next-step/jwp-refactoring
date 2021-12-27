package kitchenpos.ordertable.domain;

import kitchenpos.common.exception.MinimumOrderTableNumberException;
import kitchenpos.common.exception.NotEmptyOrderTableStatusException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("주문 테이블 컬렉션 도메인 테스트")
class OrderTablesTest {
    private OrderTable 주문테이블1번;
    private OrderTable 주문테이블2번;
    private OrderTables 주문테이블들;

    @BeforeEach
    void setUp() {
        주문테이블1번 = new OrderTable(null, 0);
        주문테이블2번 = new OrderTable(null, 0);
        주문테이블들 = new OrderTables(Lists.newArrayList(주문테이블1번, 주문테이블2번));
    }

    @DisplayName("생성 시 주문 테이블은 반드시 존재하고 2개이상 있어야 한다.")
    @Test
    void createTableGroupAtLeast2OrderTableExceptionTest() {
        assertThatThrownBy(() -> {
            // when
            주문테이블들 = new OrderTables(Lists.newArrayList(주문테이블1번));

            // then
        }).isInstanceOf(MinimumOrderTableNumberException.class);
    }

    @DisplayName("생성 시 주문 테이블은 비어있어야 한다.")
    @Test
    void createTableGroupEmptyOrderTableExceptionTest() {
        assertThatThrownBy(() -> {
            // given
            final OrderTable 주문테이블3번 = new OrderTable(null, 3);
            final OrderTable 주문테이블4번 = new OrderTable(null, 3);

            // when
            주문테이블들 = new OrderTables(Lists.newArrayList(주문테이블3번, 주문테이블4번));

            // then
        }).isInstanceOf(NotEmptyOrderTableStatusException.class);
    }
}