package kitchenpos.table.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("테이블 고객 수 테스트")
class NumberOfGuestsTest {
    @DisplayName("테이블 고객 수 생성 확인")
    @ParameterizedTest(name = "{displayName} ({index}) -> param = [{arguments}]")
    @ValueSource(ints = {0, Integer.MAX_VALUE})
    void 테이블_고객_수_생성_확인(int 요청_고객_수) {
        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> NumberOfGuests.of(요청_고객_수);

        // then
        assertThatNoException().isThrownBy(생성_요청);
    }

    @DisplayName("테이블 고객 수는 음수가 될 수 없음")
    @Test
    void 테이블_고객_수는_음수가_될_수_없음() {
        // given
        int 요청_고객_수 = -1;

        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> NumberOfGuests.of(요청_고객_수);

        // then
        assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
    }
}
