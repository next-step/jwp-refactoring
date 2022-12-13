package kitchenpos.menu.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class MenuProductTest {
    @DisplayName("메뉴상품 가격을 알 수 있다.")
    @Test
    void calculatePrice() {
        // given
        Quantity quantity = new Quantity(3L);
        Product product = new Product(new Name("불고기"), new Price(BigDecimal.valueOf(12_000)));
        MenuProduct menuProduct = new MenuProduct(quantity, product);

        // when
        Price result = menuProduct.calculatePrice();

        // then
        assertThat(result.value()).isEqualTo(BigDecimal.valueOf(3 * 12_000));
    }
}
