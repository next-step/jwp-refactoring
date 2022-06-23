package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.domain.MenuProductEntity;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 상품 목록에 대한 단위 테스트")
class MenuProductsTest {
    private ProductEntity 상품;
    private ProductEntity 상품2;

    @BeforeEach
    void setUp() {
        상품 = ProductEntity.of("test", BigDecimal.valueOf(500L));
        상품2 = ProductEntity.of("test", BigDecimal.valueOf(300L));
    }

    @DisplayName("메뉴 상품중 상품값을 안가지고 있는 값이 있으면 예외가 발생한다")
    @Test
    void exception_test() {
        // given
        MenuProductEntity menuProduct = MenuProductEntity.of(null, 상품, 3);
        MenuProductEntity menuProduct2 = MenuProductEntity.of(null, null, 3);

        // then
        assertThatThrownBy(() -> {
            new MenuProducts(Arrays.asList(menuProduct, menuProduct2), null);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 상품들의 가격의 합보다 메뉴 가격이 높으면 예외가 발생한다")
    @Test
    void exception_test2() {
        // given
        MenuProductEntity menuProduct = MenuProductEntity.of(null, 상품, 3);
        MenuProductEntity menuProduct2 = MenuProductEntity.of(null, 상품2, 3);

        // then
        Price menuPrice = new Price(BigDecimal.valueOf(2410L));
        assertThatThrownBy(() -> {
            new MenuProducts(Arrays.asList(menuProduct, menuProduct2), menuPrice);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
