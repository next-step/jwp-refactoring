package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.menu.domain.MenuProduct.Builder;
import kitchenpos.order.domain.Quantity;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {
    private MenuProduct menuProduct;
    private MenuProducts menuProducts;
    private Product product;

    @BeforeEach
    void setUp() {
        Menu menu = new Menu.Builder("menu")
                .build();

        product = new Product(1L, "product", Price.of(100L));

        menuProduct = new Builder(menu)
                .setProductId(product.getId())
                .setQuantity(Quantity.of(10L))
                .build();
        menuProducts = new MenuProducts();
        menuProducts.add(menuProduct);
    }

    @Test
    @DisplayName("메뉴 제품 객체가 같은지 검증")
    void verifyEqualsMenuProducts() {
        assertThat(menuProducts).isEqualTo(new MenuProducts(Collections.singletonList(menuProduct)));
    }

    @Test
    @DisplayName("메뉴 제품들에 잘 추가되는지 확인")
    void addMenuProduct() {
        menuProducts.add(menuProduct);

        assertThat(menuProducts.get()).hasSize(2);
    }

    @Test
    @DisplayName("메뉴 제품들의 총 합이 잘나오는지 확인")
    void totalPriceOfMenuProducts() {
        menuProducts.add(menuProduct);
        assertThat(menuProducts.totalPrice(Arrays.asList(product))).isEqualTo(Price.of(2_000L));
    }
}
