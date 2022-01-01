package kitchenpos.menu.domain;

import common.domain.Price;
import common.exception.NegativePriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("가격 도메인 테스트")
class MenuProductPriceTest {
    @DisplayName("가격이 최소값(0) 이상이어야 한다")
    @Test
    void validateTest1() {
        final BigDecimal number = BigDecimal.valueOf(-1);
        assertThatThrownBy(() -> Price.from(number))
                .isInstanceOf(NegativePriceException.class);
    }

    @DisplayName("가격이 null 이면 안된다")
    @Test
    void validateTest2() {
        assertThatThrownBy(() -> Price.from(null))
                .isInstanceOf(NegativePriceException.class);
    }
}