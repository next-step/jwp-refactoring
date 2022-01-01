package kitchenpos.domain;

import kitchenpos.common.exceptions.NegativeNumberOfGuestsException;
import kitchenpos.common.exceptions.NotEmptyOrderTableGroupException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 테이블 도메인 테스트")
class OrderTableTest {
    @DisplayName("상태 변경을 할 수 있다")
    @Test
    void changeEmptyStatusTest() {
        final OrderTable 테이블 = OrderTable.of(4, false);

        테이블.changeEmptyStatus(true);

        assertThat(테이블.isEmpty()).isTrue();
    }

    @DisplayName("상태 변경 시, 테이블 그룹에 속해 있지 않아야 한다")
    @Test
    void changeEmptyStatusTest2() {
        final TableGroup 테이블그룹 = TableGroup.from(1L);
        final OrderTable 주문테이블 = OrderTable.of(테이블그룹, 4, false);

        assertThatThrownBy(() -> 주문테이블.changeEmptyStatus(true))
                .isInstanceOf(NotEmptyOrderTableGroupException.class);
    }

    @DisplayName("손님 인원을 변경을 할 수 있다")
    @Test
    void changeNumberOfGuestsTest() {
        final OrderTable 주문테이블 = OrderTable.of(4, false);

        주문테이블.changeNumberOfGuests(10);

        assertThat(주문테이블.getNumberOfGuests().toInt()).isEqualTo(10);
    }

    @DisplayName("빈 테이블의 인원을 변경할 수 없다")
    @Test
    void changeNumberOfGuestsTest2() {
        final OrderTable 빈_테이블 = OrderTable.of(0, true);

        assertThatThrownBy(() -> 빈_테이블.changeNumberOfGuests(10))
                .isInstanceOf(NegativeNumberOfGuestsException.class);
    }

    @DisplayName("테이블 그룹을 해제할 수 있다")
    @Test
    void unGroupTest() {
        final TableGroup 테이블그룹 = TableGroup.from(1L);
        final OrderTable 주문테이블 = OrderTable.of(테이블그룹, 4, false);

        주문테이블.unGroup();

        assertThat(주문테이블.getTableGroup()).isNull();
    }
}