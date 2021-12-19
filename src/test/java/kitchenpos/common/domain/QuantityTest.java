package kitchenpos.common.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("수량 테스트")
class QuantityTest {
    @DisplayName("수량 생성 확인")
    @ParameterizedTest(name = "{displayName} ({index}) -> param = [{arguments}]")
    @ValueSource(longs = {1, Long.MAX_VALUE})
    void 수량_생성_확인(long 요청_수량) {
        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> Quantity.of(요청_수량);

        // then
        assertThatNoException().isThrownBy(생성_요청);
    }

    @DisplayName("수량 값은 Null이 될 수 없음")
    @Test
    void 수량_값은_Null이_될_수_없음() {
        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> Quantity.of(null);

        // then
        assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("수량 값은 0 또는 음수가 될 수 없음")
    @ParameterizedTest(name = "{displayName} ({index}) -> param = [{arguments}]")
    @ValueSource(longs = {0, -1, Long.MIN_VALUE})
    void 수량_값은_0_또는_음수가_될_수_없음(long 요청_수량) {
        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> Quantity.of(요청_수량);

        // then
        assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
    }
}
