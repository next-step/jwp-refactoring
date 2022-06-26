package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MenuProductTest {
    @Test
    void 가격_계산() {
        // given
        MenuProduct menuProduct = new MenuProduct(
                new Product(ProductName.from("양상추"), Price.from(1000)), Quantity.from(5)
        );

        // when
        Price result = menuProduct.calculatePrice();

        // then
        assertThat(result).isEqualTo(Price.from(5000));
    }
}
