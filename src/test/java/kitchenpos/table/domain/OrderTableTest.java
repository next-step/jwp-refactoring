package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 테이블 테스트")
class OrderTableTest {

    @Test
    void 주문_테이블_생성_시_단체_지정은_되어있지_않다() {
        OrderTable actual = OrderTable.of(0, true);

        assertAll(() -> {
            assertThat(actual).isNotNull();
            assertThat(actual.getTableGroupId()).isNull();
        });
    }

    @Test
    void 방문한_손님_수는_0명_이상이어야_한다() {
        ThrowingCallable throwingCallable = () -> OrderTable.of(-1, true);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable)
                .withMessage("방문한 손님 수는 0명 이상이어야 합니다.");
    }

    @Test
    void 방문한_손님_수를_변경한다() {
        // given
        OrderTable actual = OrderTable.of(1, false);

        // when
        actual.changeNumberOfGuests(5);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    void 방문한_손님_수_변경_시_빈테이블이면_변경할_수_없다() {
        // given
        OrderTable actual = OrderTable.of(1, true);

        // when
        ThrowingCallable throwingCallable = () -> actual.changeNumberOfGuests(5);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable)
                .withMessage("주문 테이블이 빈 테이블이면 방문자 수를 변경할 수 없습니다.");
    }
}
