package kitchenpos.domain;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class MenuProductTest {

    @Test
    void 메뉴_상품의_가격을_계산한다() {
        // given
        MenuProduct menuProduct = new MenuProduct(new Product("치킨", BigDecimal.valueOf(5000)), 5);

        // when
        Price result = menuProduct.price();

        // then
        assertThat(result.value()).isEqualTo(BigDecimal.valueOf(25000));
    }
}
