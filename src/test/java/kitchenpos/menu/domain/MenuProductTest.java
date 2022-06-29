package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuProductTest {
    @Test
    @DisplayName("메뉴 상품을 생성한다.")
    void createMenuProduct() {
        Product product = Product.of("허니콤보", 19_000L);

        MenuProduct menuProduct = MenuProduct.createMenuProduct(product.getId(), 1L);

        assertThat(menuProduct.getProductId()).isEqualTo(product.getId());
    }
}
