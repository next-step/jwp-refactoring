package kitchenpos.unit;

import kitchenpos.product.domain.ProductPrice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductPriceTest {

    @Test
    @DisplayName("가격이 0원 미만일 수 없습니다")
    void priceZeroNo() {
        assertThatThrownBy(() ->
                new ProductPrice(BigDecimal.valueOf(-1))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
