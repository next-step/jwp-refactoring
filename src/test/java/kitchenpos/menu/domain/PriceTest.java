package kitchenpos.menu.domain;

import kitchenpos.menu.application.exception.InvalidPrice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("가격 테스트")
class PriceTest {

    @Test
    @DisplayName("메뉴의 가격이 null인 경우 예외가 발생한다.")
    void validatePriceNull() {
        assertThatThrownBy(() -> new Price(null))
                .isInstanceOf(InvalidPrice.class);
    }

    @Test
    @DisplayName("메뉴의 가격이 0원 미만인 경우 예외가 발생한다.")
    void validateMinPrice() {
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-1)))
                .isInstanceOf(InvalidPrice.class);
    }
}
