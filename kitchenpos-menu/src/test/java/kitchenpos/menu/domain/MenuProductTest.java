package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {
    @DisplayName("메뉴상품의 가격을 계산할 수 있다.")
    @Test
    void calculatePrice() {
        MenuProduct menuProduct = new MenuProduct(1L, 2);

        MenuPrice actual = menuProduct.calculatePrice(BigDecimal.valueOf(8000));

        assertThat(actual.value()).isEqualTo(BigDecimal.valueOf(16000));
    }
}
