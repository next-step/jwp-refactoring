package kitchenpos.common.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("가격 테스트")
class PriceTest {
    @DisplayName("가격 생성 확인")
    @ParameterizedTest(name = "{displayName} ({index}) -> param = [{arguments}]")
    @ValueSource(longs = {0, Long.MAX_VALUE})
    void 가격_생성_확인(long price) {
        // given
        BigDecimal 생성_요청_가격 = BigDecimal.valueOf(price);

        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> Price.of(생성_요청_가격);

        // then
        assertThatNoException().isThrownBy(생성_요청);
    }

    @DisplayName("가격 값은 Null이 될 수 없음")
    @Test
    void 가격_값은_Null이_될_수_없음() {
        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> Price.of(null);

        // then
        assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격 값은 음수가 될 수 없음")
    @ParameterizedTest(name = "{displayName} ({index}) -> param = [{arguments}]")
    @ValueSource(longs = {-1, Long.MIN_VALUE})
    void 가격_값은_음수가_될_수_없음(long price) {
        // given
        BigDecimal 생성_요청_가격 = BigDecimal.valueOf(price);

        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> Price.of(생성_요청_가격);

        // then
        assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
    }
}
