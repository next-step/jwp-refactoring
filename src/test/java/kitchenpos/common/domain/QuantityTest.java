package kitchenpos.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("수량 테스트")
class QuantityTest {

    @DisplayName("수량 생성 성공 테스트")
    @ParameterizedTest(name = "{displayName}{index} -> quantity: {0}")
    @ValueSource(ints = {0, 1_000})
    void instantiate_success(long quantity) {
        // when & then
        assertThat(Quantity.of(quantity)).isExactlyInstanceOf(Quantity.class);
    }

    @DisplayName("수량 생성 실패 테스트 - 음수")
    @Test
    void instantiate_success_negative() {
        // given
        long quantity = -1;

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Quantity.of(quantity));
    }
}
