package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.Test;

class MenuProductTest {
    @Test
    void 생성() {
        MenuProduct menuProduct = new MenuProduct(3L, 1);

        assertAll(
                () -> assertThat(menuProduct.getProductId()).isEqualTo(3L),
                () -> assertThat(menuProduct.getQuantity()).isEqualTo(1)
        );
    }
}
