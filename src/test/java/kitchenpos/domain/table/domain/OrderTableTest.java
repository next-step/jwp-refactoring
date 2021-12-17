package kitchenpos.domain.table.domain;

import kitchenpos.exception.BusinessException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class OrderTableTest {

    @Test
    void changeEmpty_빈_테이블_여부를_변경한다() {
        OrderTable orderTable = 채워진_테이블();
        orderTable.changeEmpty(true);
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    void checkInTableGroup_테이블_그룹에_존재하면_에러를_발생한다() {
        OrderTable orderTable = new OrderTable(1L, 10, false);
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> orderTable.validateTableGroup());
    }

    @Test
    void changeNumberOfGuests_테이블_손님_수를_변경한다() {
        OrderTable orderTable = 채워진_테이블();
        orderTable.changeNumberOfGuests(10);
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);
    }

    @Test
    void changeNumberOfGuests_음수_테이블_손님_수로_변경하면_에러를_발생한다() {
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> 채워진_테이블().changeNumberOfGuests(-10));
    }

    @Test
    void checkNonEmptyInGroup_빈_테이블이_아니면_에러를_발생한다() {
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> 채워진_테이블().checkNonEmptyInGroup());
    }

    @Test
    void checkNonEmptyInGroup_테이블_그룹에_속하지_않으면_에러를_발생한다() {
        OrderTable orderTable = new OrderTable(1L, 10, false);
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> orderTable.checkNonEmptyInGroup());
    }

    private OrderTable 채워진_테이블() {
        return new OrderTable(null, 5, false);
    }

    private OrderTable 빈_테이블() {
        return new OrderTable(null, 0, true);
    }
}
