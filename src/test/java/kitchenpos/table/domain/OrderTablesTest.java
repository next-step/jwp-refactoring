package kitchenpos.table.domain;

import kitchenpos.common.fixtrue.OrderTableFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class OrderTablesTest {

    OrderTables orderTables;
    OrderTable firstOrderTable;
    OrderTable secondOrderTable;

    @BeforeEach
    void setUp() {
        firstOrderTable = OrderTableFixture.of(2, true);
        secondOrderTable = OrderTableFixture.of(2, true);
        orderTables = OrderTables.from(Arrays.asList(firstOrderTable, secondOrderTable));
    }

    @Test
    void 단체_지정_시_주문_테이블은_두_테이블_이상이어야_한다() {
        // given - when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> OrderTables.from(Collections.singletonList(firstOrderTable));

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable)
                .withMessage("단체 지정 시 주문 테이블은 두 테이블 이상이어야 합니다.");
    }

    @Test
    void 단체_지정_시_주문_테이블은_빈_테이블이어야_한다() {
        // given
        OrderTable firstGroupedOrderTable = OrderTable.of(2, false);
        OrderTable secondGroupedOrderTable = OrderTable.of(2, false);

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> OrderTables.from(Arrays.asList(firstGroupedOrderTable, secondGroupedOrderTable));

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable)
                .withMessage("단체 지정 시 주문 테이블은 빈 테이블 이어야 합니다.");
    }

}
