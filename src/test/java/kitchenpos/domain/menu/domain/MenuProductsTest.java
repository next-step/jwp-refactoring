package kitchenpos.domain.menu.domain;

import kitchenpos.domain.menu.domain.MenuProduct;
import kitchenpos.domain.menu.domain.MenuProducts;
import kitchenpos.domain.product.domain.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class MenuProductsTest {

    private static final MenuProducts menuProducts = new MenuProducts(Arrays.asList(
            new MenuProduct(new Product("상품", 10_000), 10),
            new MenuProduct(new Product("상품", 20_000), 10)));

    @Test
    void calculateSum_메뉴상품_목록의_모든_가격의_합을_계산한다() {
        assertThat(menuProducts.calculateSum()).isEqualTo(BigDecimal.valueOf(300_000));
    }
}
