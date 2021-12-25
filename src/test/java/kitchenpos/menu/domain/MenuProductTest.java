package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 상품 테스트")
class MenuProductTest {

    @DisplayName("메뉴 상품 생성 성공 테스트")
    @Test
    void instantiate_success() {
        // given
        Product product = Product.of("강정치킨", BigDecimal.valueOf(17_000));
        Quantity quantity = Quantity.of(2);

        // when
        MenuProduct menuProduct = MenuProduct.of(product, quantity);

        // then
        assertAll(
                () -> assertThat(menuProduct).isNotNull()
                , () -> assertThat(menuProduct.getProduct()).isEqualTo(product)
                , () -> assertThat(menuProduct.getQuantity()).isEqualTo(quantity)
        );
    }

    @DisplayName("메뉴 상품 합계 테스트")
    @Test
    void getTotalPrice() {
        // given
        BigDecimal.valueOf(17_000);
        Product product = Product.of("강정치킨", BigDecimal.valueOf(17_000));
        Quantity quantity = Quantity.of(2);
        MenuProduct menuProduct = MenuProduct.of(product, quantity);

        // when & then
        assertThat(menuProduct.getTotalPrice()).isEqualTo(Price.of(BigDecimal.valueOf(34_000)));
    }
}
