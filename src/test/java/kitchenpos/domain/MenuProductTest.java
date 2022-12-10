package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

class MenuProductTest {
    @Test
    void 생성() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setMenuId(2L);
        menuProduct.setProductId(3L);
        menuProduct.setQuantity(1);

        assertAll(
                () -> assertThat(menuProduct.getSeq()).isEqualTo(1L),
                () -> assertThat(menuProduct.getMenuId()).isEqualTo(2L),
                () -> assertThat(menuProduct.getProductId()).isEqualTo(3L),
                () -> assertThat(menuProduct.getQuantity()).isEqualTo(1)
        );
    }
}
