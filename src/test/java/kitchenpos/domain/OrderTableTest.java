package kitchenpos.domain;

import static kitchenpos.fixture.DomainFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        assertThatThrownBy(() -> orderTable.changeEmpty(true)).isInstanceOf(IllegalArgumentException.class);
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
                IllegalArgumentException.class);
    }
}
