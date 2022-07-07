package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블")
class OrderTableTest {
    @DisplayName("빈 테이블 상태를 변경할 수 있다.")
    @Test
    void 빈_테이블_상태_변경_성공() {
        OrderTable 주문_테이블 = OrderTable.of(10, true);
        주문_테이블.changeEmpty(false);

        assertThat(주문_테이블.isEmpty()).isFalse();
    }

    @DisplayName("단체 지정된 테이블의 경우 빈 테이블 상태를 변경할 수 없다.")
    @Test
    void 빈_테이블_상태_변경_실패() { // Long id, Long tableGroupId, int numberOfGuests, boolean empty
        OrderTable 주문_테이블 = new OrderTable(1L, 1L, 5, false);
        assertThatThrownBy(() -> 주문_테이블.changeEmpty(true)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수를 변경할 수 있다.")
    @Test
    void 손님_수_변경_성공() {
        OrderTable 주문_테이블 = OrderTable.of(10, false);
        주문_테이블.changeNumberOfGuests(NumberOfGuests.from(5));

        assertThat(주문_테이블.getNumberOfGuests()).isEqualTo(5);
    }

    @DisplayName("빈 테이블의 손님 수를 변경할 수 없습니다.")
    @Test
    void 빈_테이블_손님_수_변경_실패() {
        OrderTable 주문_테이블 = OrderTable.of(10, true);
        assertThatThrownBy(() -> 주문_테이블.changeNumberOfGuests(NumberOfGuests.from(5))).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("단체 지정할 수 있다.")
    @Test
    void 단체_지정() {
        OrderTable 주문_테이블 = OrderTable.of(10, true);
        주문_테이블.setGroup(1L);

        assertThat(주문_테이블.getTableGroupId()).isEqualTo(1L);
    }

    @DisplayName("단체 지정을 리셋할 수 있다.")
    @Test
    void 단체_지정_리셋() {
        OrderTable 주문_테이블 = new OrderTable(1L, 1, false);
        주문_테이블.resetGroup();

        assertThat(주문_테이블.getTableGroupId()).isNull();
    }
}
