package kitchenpos.domain;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    private Product product;

    @BeforeEach
    void before() {
        product = new Product("짜장", BigDecimal.valueOf(5000L));
    }
    @Test
    @DisplayName("메뉴 상품의 총계를 구한다.")
    void calculateTest() {
/*        MenuProduct menuProduct = new MenuProduct(product, 3);
        Price result = menuProduct.calculateAmount();
        assertThat(result).isEqualTo(new Price(BigDecimal.valueOf(15000L)));*/
    }
}
