package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class QuantityTest {

    @ParameterizedTest(name = "\"{0}\" 일 경우")
    @DisplayName("Quantity 생성시 유효성 검사를 체크한다.")
    @NullSource
    @ValueSource(longs = -1)
    void createFail(Long quantity) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new Quantity(quantity));
    }

}
