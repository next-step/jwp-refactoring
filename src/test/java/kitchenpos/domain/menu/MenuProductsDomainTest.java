package kitchenpos.domain.menu;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;

public class MenuProductsDomainTest {
    @DisplayName("주어진 개수를 곱한 여러 상품가격이 계산된다.")
    @Test
    void calculate_multiProductPriceWithQuantity() {
        // given
        MenuProduct 메뉴상품1 = MenuProduct.of(Product.of("메뉴1", Price.of(1_800)), 2);
        MenuProduct 메뉴상품2 = MenuProduct.of(Product.of("메뉴1", Price.of(1_500)), 3);

        MenuProducts menuProducts = MenuProducts.of(List.of(메뉴상품1, 메뉴상품2));

        // when
        Price calculatedPrice = menuProducts.getSumProductPrice();

        // then
        Assertions.assertThat(calculatedPrice).isEqualTo(Price.of(8_100));
    }
}
