package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.core.exception.CannotCreateException;
import kitchenpos.core.exception.ExceptionType;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 상품 목록에 대한 단위 테스트")
class MenuProductsTest {
    private Product 상품;
    private Product 상품2;

    @BeforeEach
    void setUp() {
        상품 = Product.of("test", BigDecimal.valueOf(500L));
        상품2 = Product.of("test", BigDecimal.valueOf(300L));
    }

    @DisplayName("메뉴 상품중 상품값을 안가지고 있는 값이 있으면 예외가 발생한다")
    @Test
    void exception_test() {
        // given
        MenuProduct menuProduct = MenuProduct.of(1L, 3);
        MenuProduct menuProduct2 = MenuProduct.of(null, 3);

        // then
        assertThatThrownBy(() -> {
            new MenuProducts(Arrays.asList(menuProduct, menuProduct2), null, null);
        }).isInstanceOf(CannotCreateException.class)
            .hasMessageContaining(ExceptionType.CONTAINS_NOT_EXIST_PRODUCT.getMessage());
    }

    @DisplayName("메뉴 상품들의 가격의 합보다 메뉴 가격이 높으면 예외가 발생한다")
    @Test
    void exception_test2() {
        // given
        MenuProduct menuProduct = MenuProduct.of(1L, 3);
        MenuProduct menuProduct2 = MenuProduct.of(2L, 3);

        // then
        Price menuPrice = new Price(BigDecimal.valueOf(2410L));
        assertThatThrownBy(() -> {
            new MenuProducts(Arrays.asList(menuProduct, menuProduct2), menuPrice, null);
        }).isInstanceOf(CannotCreateException.class)
            .hasMessageContaining(ExceptionType.IS_NOT_OVER_THAN_MENU_PRICE.getMessage());
    }
}
