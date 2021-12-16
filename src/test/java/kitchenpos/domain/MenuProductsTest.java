package kitchenpos.domain;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.product.Product;

@DisplayName("메뉴상품 전문관련")
public class MenuProductsTest {
    @DisplayName("상품의 가격합계를 계산한다.")
    @Test
    void calculate_sumProductPrice() {
        // given
        Product 상품1 = Product.of(1L, "상품1", Price.of(1_200));
        Product 상품2 = Product.of(2L, "상품2", Price.of(2_200));

        MenuProducts menuProducts = MenuProducts.of(List.of(MenuProduct.of(상품1, 1L), MenuProduct.of(상품2, 2L)));

        // when
        Price price = menuProducts.getSumProductPrice();

        // then
        Assertions.assertThat(price).isEqualTo(Price.of(5600));
    }
}
