package kitchenpos.menu.domain;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductPrice;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @DisplayName("메뉴 상품 생성")
    @Test
    void construct() {
        Product 타코야끼 = new Product(1L, "타코야끼", new ProductPrice(BigDecimal.valueOf(12000)));
        MenuProduct menuProduct = new MenuProduct(타코야끼, 2);
        MenuProduct expectedMenuProduct = new MenuProduct(타코야끼, 2);
        Assertions.assertThat(menuProduct.getProduct()).isEqualTo(expectedMenuProduct.getProduct());
    }
}
