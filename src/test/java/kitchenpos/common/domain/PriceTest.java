package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class PriceTest {

    @ParameterizedTest(name = "{0} 일 경우")
    @NullSource
    @ValueSource(strings = "-1")
    void createFail(BigDecimal value) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new Price(value));
    }

    @Test
    @DisplayName("입력값보다 더 큰지 체크한다.")
    void isGreaterThan() {
        Price price = new Price(BigDecimal.ONE);
        assertThat(price.isGreaterThan(BigDecimal.ZERO)).isTrue();
    }

}
