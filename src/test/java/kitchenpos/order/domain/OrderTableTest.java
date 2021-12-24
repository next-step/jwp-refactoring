package kitchenpos.order.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 테이블 테스트")
class OrderTableTest {

    @Test
    void 주문_테이블_생성_시_단체_지정은_안되어있다() {
        OrderTable actual = OrderTable.of(1L, 1, true);

        assertAll(() -> {
            assertThat(actual).isNotNull();
            assertThat(actual.getTableGroupId()).isNull();
        });
    }

    @Test
    void 방문한_손님_수는_0명_이상이어야_한다() {
        ThrowingCallable throwingCallable = () -> OrderTable.of(1L, -1, true);

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable)
                .withMessage("방문한 손님 수는 0명 이상이어야 합니다.");
    }
}
