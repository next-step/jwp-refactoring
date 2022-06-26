package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.order.domain.Quantity;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    @DisplayName("메뉴 상품이 같은지 검증")
    void verifyEqualsMenuProduct() {
        final Product product = new Product(null, null, Price.of(100L));
        final Menu menu = new Menu.Builder("메뉴")
                .setId(1L)
                .setPrice(Price.of(100L))
                .build();
        final MenuProduct menuProduct = new MenuProduct.Builder(menu)
                .setSeq(1L)
                .setProduct(product)
                .setQuantity(Quantity.of(1L))
                .build();

        assertThat(menuProduct).isEqualTo(new MenuProduct.Builder(menu)
                .setSeq(1L)
                .setProduct(product)
                .setQuantity(Quantity.of(1L))
                .build());
    }
}
