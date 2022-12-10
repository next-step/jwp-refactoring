package kitchenpos.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuProductsTest {
    @DisplayName("전체 메뉴 상품의 총 가격의 합을 알 수 있다.")
    @Test
    void totalPrice() {
        // given
        Product product1 = new Product("불고기", new Price(BigDecimal.valueOf(12_000)));
        Product product2 = new Product("잡채", new Price(BigDecimal.valueOf(8_000)));
        MenuProduct menuProduct1 = new MenuProduct(1L, product1);
        MenuProduct menuProduct2 = new MenuProduct(1L, product2);
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(menuProduct1, menuProduct2));

        // when
        Price result = menuProducts.totalMenuPrice();

        // then
        assertThat(result.value()).isEqualTo(BigDecimal.valueOf(2_0000));
    }
}
