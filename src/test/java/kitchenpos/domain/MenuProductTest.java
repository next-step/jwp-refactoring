package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @DisplayName("총 금액 계산")
    @Test
    void calculateTotal() {
        MenuProduct menuProduct = new MenuProduct(new Product("product", BigDecimal.TEN), 4);

        assertThat(menuProduct.calculateTotal()).isEqualTo(new Price(BigDecimal.valueOf(40)));
    }
}