package kitchenpos.menu.domain;

import kitchenpos.common.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 상품 테스트")
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