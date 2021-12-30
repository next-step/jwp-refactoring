package kitchenpos.table.domain;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.IllegalArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DisplayName("주문 테이블 도메인 테스트")
public class OrderTableTest {
    @DisplayName("주문 테이블을 빈 테이블 상태로 변경한다.")
    @Test
    void 테이블_빈_테이블로_변경() {
        OrderTable orderTable = OrderTable.of(10, false);

        orderTable.changeEmpty(true);

        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("그루핑되어 있을 경우 빈 테이블 상태로 변경 불가하다.")
    @Test
    void 그룹핑_테이블_빈_테이블로_변경_예외() {
        OrderTable orderTable = OrderTable.of(10, false);
        orderTable.addGroup(1L);

        Throwable thrown = catchThrowable(() -> orderTable.changeEmpty(true));

        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage("테이블 그룹이 존재하므로 빈 테이블 설정을 할 수 없습니다.");
    }

    @DisplayName("주문 테이블 손님 수를 변경한다.")
    @Test
    void 테이블_손님_수_변경() {
        OrderTable orderTable = OrderTable.of(10, false);

        orderTable.changeNumberOfGuests(2);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(2);
    }

    @DisplayName("빈 테이블의 손님 수 변경 시 0미만일 경우 불가하다.")
    @Test
    void 테이블_0미만_손님_수_변경_예외() {
        OrderTable orderTable = OrderTable.of(0, false);

        Throwable thrown = catchThrowable(() -> orderTable.changeNumberOfGuests(-1));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님 수는 최소 0명 이상 설정 가능합니다.");
    }

    @DisplayName("빈 테이블의 손님 수 변경 불가하다.")
    @Test
    void 빈_테이블_손님_수_변경_예외() {
        OrderTable orderTable = OrderTable.of(0, true);

        Throwable thrown = catchThrowable(() -> orderTable.changeNumberOfGuests(5));

        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage("빈 테이블의 손님 수를 설정할 수 없습니다.");
    }
}
