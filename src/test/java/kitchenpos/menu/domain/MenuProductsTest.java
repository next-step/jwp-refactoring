package kitchenpos.menu.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuProductsTest {

    @Test
    @DisplayName("메뉴 상품 목록 생성")
    void menuProducts() {
        // given
        Product 피자 = Product.of(Name.of("피자"), Price.of(BigDecimal.valueOf(17_000)));
        Quantity 수량 = Quantity.of(2);
        MenuProduct 메뉴_상품 = MenuProduct.of(피자.getId(), 수량);

        // when
        MenuProducts 메뉴_상품_목록 = MenuProducts.of(Arrays.asList(메뉴_상품));

        // then
        assertAll(
                () -> assertThat(메뉴_상품_목록).isNotNull(),
                () -> assertThat(메뉴_상품_목록.getMenuProducts()).isEqualTo(Arrays.asList(메뉴_상품))
        );
    }
}
