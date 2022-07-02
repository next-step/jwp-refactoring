package kitchenpos.product.domain;

import kitchenpos.product.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("가격 단위테스트")
class PriceTest {
    @DisplayName("가격은 0원 이상이어야 한다")
    @Test
    void createPriceZero() {
        assertThatThrownBy(() -> Price.valueOf(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
