package kitchenpos.table.unit;

import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTableTest {

    @DisplayName("주문 테이블 생성에 성공한다.")
    @Test
    void 생성() {
        // when
        OrderTable orderTable = new OrderTable(null, 0, false);

        // then
        assertThat(orderTable).isNotNull();
        assertThat(orderTable.getNumberOfGuests()).isZero();
        assertThat(orderTable.getEmpty()).isFalse();
    }

    @DisplayName("주문 테이블을 빈 테이블로 설정하는 데 성공한다.")
    @Test
    void 빈_테이블로_설정() {
        // given
        OrderTable orderTable = new OrderTable(null, 0, false);

        // when
        orderTable.changeEmpty(true);

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }


    @DisplayName("테이블이 단체 지정되어 있으면 빈 테이블로 설정하는 데 실패한다")
    @Test
    void 빈_테이블로_설정_예외_단체_지정됨() {
        // given
        OrderTable 테이블1 = new OrderTable(null, 0, true);
        OrderTable 테이블2 = new OrderTable(null, 0, true);
        TableGroupTest.테이블_단체_지정되어_있음(테이블1, 테이블2);

        // when, then
        assertThatThrownBy(() -> 테이블1.changeEmpty(true))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("손님 수 변경에 성공한다.")
    @Test
    void 손님_수_변경() {
        // given
        OrderTable orderTable = new OrderTable(null, 0, false);

        // when
        orderTable.changeNumberOfGuests(7);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(7);
    }

    @DisplayName("손님 수가 0 미만이면 손님 수 변경에 실패한다.")
    @Test
    void 손님_수_변경_예외_0_미만() {
        // given
        OrderTable orderTable = new OrderTable(null, 0, false);

        // when, then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블이면 손님 수 변경에 실패한다.")
    @Test
    void 손님_수_변경_예외_빈_테이블() {
        // given
        OrderTable orderTable = new OrderTable(null, 0, true);

        // when, then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(7))
                .isInstanceOf(IllegalStateException.class);
    }

    public static OrderTable 테이블_생성되어_있음(int numberOfGuests, boolean empty) {
        return new OrderTable(null, numberOfGuests, empty);
    }
}
