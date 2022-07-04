package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.order.domain.Quantity;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {
    private Product product;
    private Menu menu;
    private MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        product = new Product(1L, null, Price.of(100L));
        menu = new Menu.Builder("메뉴")
                .setId(1L)
                .setPrice(Price.of(100L))
                .build();
        menuProduct = new MenuProduct.Builder(menu)
                .setSeq(1L)
                .setProductId(product.getId())
                .setQuantity(Quantity.of(1L))
                .build();
    }

    @Test
    @DisplayName("메뉴 상품이 같은지 검증")
    void verifyEqualsMenuProduct() {
        assertThat(menuProduct).isEqualTo(new MenuProduct.Builder(menu)
                .setSeq(1L)
                .setProductId(product.getId())
                .setQuantity(Quantity.of(1L))
                .build());
    }

    @Test
    @DisplayName("메뉴 상품의 가격이 맞는지 검증")
    void verifyMenuProductPrice() {
        final Price expectedPrice = product.getPrice().multiply(menuProduct.quantity().value());

        assertThat(menuProduct.price(product.getPrice())).isEqualTo(expectedPrice);
    }
}
