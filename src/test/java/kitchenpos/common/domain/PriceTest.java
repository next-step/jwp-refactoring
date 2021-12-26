package kitchenpos.common.domain;

import kitchenpos.common.exception.NegativePriceException;
import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("가격 도메인 테스트")
class PriceTest {
    private Price price;

    @DisplayName("가격은 0 미만이 될 수 없다.")
    @Test
    void createPriceNegativePriceExceptionTest() {
        assertThatThrownBy(() -> {
            // when
            Price price = new Price(BigDecimal.valueOf(-1));

            // then
        }).isInstanceOf(NegativePriceException.class);
    }
}