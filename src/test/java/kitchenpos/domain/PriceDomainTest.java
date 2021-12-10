package kitchenpos.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

public class PriceDomainTest {
    @DisplayName("0보다 작은 값으로 가격을 생성시 예외가 발생된다.")
    @Test
    void exception_PriceGenerate_negativeNumber() {
        // given
        int number = -1;

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> Price.of(number));
    }
}
