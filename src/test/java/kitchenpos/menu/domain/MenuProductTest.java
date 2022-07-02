package kitchenpos.menu.domain;

import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.common.Quantity;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuProductTest {

    @DisplayName("MenuProduct 인스턴스를 생성한다")
    @Test
    void of() throws Exception {
        // given
        Product product = new Product(1L, new Name("진매"), new Price(1000));
        Quantity quantity = new Quantity(10);

        // when
        MenuProduct menuProduct = MenuProduct.of(product, quantity.value());

        // then
        assertAll(
                () -> assertThat(menuProduct).isNotNull(),
                () -> assertThat(menuProduct.getProduct()).isEqualTo(product),
                () -> assertThat(menuProduct.getQuantity()).isEqualTo(quantity)
        );
    }
}
