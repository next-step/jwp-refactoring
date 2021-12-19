package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@DisplayName("메뉴 상품 테스트")
class MenuProductTest {

    @Test
    @DisplayName("상품 금액을 계산한다.")
    void calculateAmount() {
        // given
        Product product = new Product("후라이드치킨", new BigDecimal(16_000));
        MenuProduct menuProduct = new MenuProduct(product, 3);

        // when
        Price amount = menuProduct.calculateAmount();

        // then
        assertThat(amount).isEqualTo(new Price(48_000));
    }
}
