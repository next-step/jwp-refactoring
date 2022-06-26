package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    @DisplayName("메뉴 상품이 같은지 검증")
    void verifyEqualsMenuProduct() {
        final Product product = new Product(null, null, null);
        final Menu menu = new Menu(1L, null, null, null, null);
        final MenuProduct menuProduct = new MenuProduct(1L, menu, product, 1L);

        assertThat(menuProduct).isEqualTo(new MenuProduct(1L, menu, product, 1L));
    }
}
