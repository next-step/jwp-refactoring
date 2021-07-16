package kitchenpos.menu.domain;

import kitchenpos.common.valueobject.Price;
import kitchenpos.menu.domain.exception.BadMenuPriceException;
import kitchenpos.product.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuProductsTest {

    @DisplayName("메뉴의 총 가격이 기존 상품들의 총합 가격보다 비쌀 수 없다.")
    @Test
    void createMenuExceptionIfMenuPriceHigherThanProductTotalPrice() {
        //given
        Product 후라이드 = Product.of("후라이드", BigDecimal.valueOf(10000L));
        MenuProduct 메뉴_후라이드 = MenuProduct.of(후라이드, 3);
        MenuProducts menuProducts = MenuProducts.of(Lists.list(메뉴_후라이드));

        //when
        assertThatThrownBy(() -> menuProducts.validatePrice(Price.of(BigDecimal.valueOf(40000))))
                .isInstanceOf(BadMenuPriceException.class); //then
    }
}
