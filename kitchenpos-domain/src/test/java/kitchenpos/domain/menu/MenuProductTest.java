package kitchenpos.domain.menu;

import kitchenpos.domain.valueobject.Price;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MenuProductTest {

    @Test
    @DisplayName("해당 메뉴의 가격을 계산할 수 있다.")
    public void calculatePrice() throws Exception {
        // given
        Product product = new Product("후라이드", Price.of(16000));
        MenuProduct menuProduct = new MenuProduct(product, 2L);

        // when
        Price price = menuProduct.calculatePrice();

        // then
        assertThat(price).isNotNull();
        assertThat(price).isEqualTo(Price.of(32000));
    }
}
