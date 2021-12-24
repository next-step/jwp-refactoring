package kitchenpos.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("가격 테스트")
class PriceTest {

    @DisplayName("가격 생성 성공 테스트")
    @ParameterizedTest(name = "{displayName}{index} -> price: {0}")
    @ValueSource(ints = {0, 1_000})
    void instantiate_success(int value) {
        // given
        BigDecimal price = BigDecimal.valueOf(value);

        // when & then
        assertThat(Price.of(price)).isExactlyInstanceOf(Price.class);
    }

    @DisplayName("가격 생성 실패 테스트 - 음수")
    @Test
    void instantiate_success_negative() {
        // given
        BigDecimal price = BigDecimal.valueOf(-1);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Price.of(price));
    }

    @DisplayName("가격 생성 실패 테스트 - null")
    @Test
    void instantiate_failure_null() {
        // given
        BigDecimal price = null;

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Price.of(price));
    }
}
