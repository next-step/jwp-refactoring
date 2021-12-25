package kitchenpos.order.domain;

import kitchenpos.common.fixtrue.OrderTableFixture;
import kitchenpos.common.fixtrue.TableGroupFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("단체 지정 테스트")
class TableGroupTest {

    List<OrderTable> orderTables;
    OrderTable firstOrderTable;
    OrderTable secondOrderTable;

    @BeforeEach
    void setUp() {
        firstOrderTable = OrderTableFixture.of(2, true);
        secondOrderTable = OrderTableFixture.of(2, true);
        orderTables = Arrays.asList(firstOrderTable, secondOrderTable);
    }

    @Test
    void 단체_지정() {
        TableGroup actual = TableGroupFixture.of(1L, firstOrderTable, secondOrderTable);

        assertAll(() -> {
            assertThat(actual).isNotNull();
            assertThat(actual.getOrderTables()).hasSize(2);
        });
    }

    @Test
    void 단체_지정_시_주문_테이블은_두_테이블_이상이어야_한다() {
        ThrowableAssert.ThrowingCallable throwingCallable = () -> TableGroupFixture.of(1L, firstOrderTable);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable)
                .withMessage("단체 지정 시 주문테이블은 두 테이블 이상 이어야 합니다.");
    }
}
