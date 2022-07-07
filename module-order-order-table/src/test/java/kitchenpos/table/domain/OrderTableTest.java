package kitchenpos.table.domain;

import static kitchenpos.fixture.OrderTableFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.exception.OrderTableAlreadyEmptyException;
import kitchenpos.exception.OrderTableAlreadyTableGroupException;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderTableTest {
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        // given
        orderTable = createOrderTable(1L, null, 4, false);
    }

    @Test
    void 빈_테이블로_변경() {
        // when
        orderTable.changeEmpty(true);
        // then
        assertThat(orderTable.isEmpty()).isEqualTo(true);
    }

    @Test
    void 빈_테이블로_변경_단체_테이블_예외() {
        // given
        orderTable.groupByTableGroupId(1L);
        // when, then
        assertThatThrownBy(() -> orderTable.changeEmpty(true)).isInstanceOf(OrderTableAlreadyTableGroupException.class);
    }

    @Test
    void 손님_수_변경() {
        // when
        orderTable.changeNumberOfGuests(NumberOfGuests.from(5));
        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(NumberOfGuests.from(5));
    }

    @Test
    void 손님_수_변경_빈_테이블_예외() {
        // given
        orderTable.changeEmpty(true);
        // when, then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(NumberOfGuests.from(10))).isInstanceOf(
                OrderTableAlreadyEmptyException.class);
    }

    @Test
    void 단체_지정() {
        // when
        orderTable.groupByTableGroupId(1L);
        // then
        assertAll(
                () -> assertThat(orderTable.isGroupedByTableGroup()).isEqualTo(true),
                () -> assertThat(orderTable.isEmpty()).isEqualTo(false)
        );
    }

    @Test
    void 단체_삭제() {
        // given
        orderTable.groupByTableGroupId(1L);
        // when
        orderTable.unGroup();
        // then
        assertAll(
                () -> assertThat(orderTable.isGroupedByTableGroup()).isEqualTo(false)
        );
    }
}
