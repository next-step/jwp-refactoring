package kitchenpos.table.unit;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.exception.ErrorMessage;
import kitchenpos.common.vo.Empty;
import kitchenpos.common.vo.GuestCount;
import kitchenpos.table.domain.OrderTable;

@DisplayName("주문테이블 관련 단위테스트")
public class OrderTableTest {
    @DisplayName("주문테이블을 생성할 수 있다.")
    @Test
    void createOrderTable() {
        // when
        OrderTable 채워진_테이블 = OrderTable.of(2, false);
        // then
        assertAll(
            () -> assertThat(채워진_테이블.getEmpty()).isEqualTo(Empty.of(false)),
            () -> assertThat(채워진_테이블.getGuestCounts()).isEqualTo(GuestCount.of(2))
        );
    }

    @DisplayName("주문테이블의 손님수를 변경할 수 있다.")
    @Test
    void updateNumberOfGuest() {
        // given
        OrderTable 채워진_테이블 = OrderTable.of(2, false);
        // when
        채워진_테이블.updateNumberOfGuest(GuestCount.of(3));
        // then
        assertThat(채워진_테이블.getGuestCounts()).isEqualTo(GuestCount.of(3));
    }

    @DisplayName("주문테이블을의 비움 상태를 변경할 수 있다.")
    @Test
    void updateEmptyStatus() {
        // given
        OrderTable 채워진_테이블 = OrderTable.of(2, false);
        // when
        채워진_테이블.updateEmptyStatus(Empty.of(true));
        // then
        assertThat(채워진_테이블.getEmpty()).isEqualTo(Empty.of(true));
    }

    @DisplayName("빈테이블의 손님수를 변경할때 예외가 발생한다.")
    @Test
    void updateNumberOfGuest_when_empty_exception() {
        // given
        OrderTable 빈_테이블 = OrderTable.of(2, true);
        // when - then
        assertThatThrownBy(() -> 빈_테이블.updateNumberOfGuest(GuestCount.of(3)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ErrorMessage.CANNOT_CHANGE_NUMBER_OF_GUESTS_WHEN_TABLE_IS_EMPTY);
    }

}
