package kitchenpos.menu;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import kitchenpos.common.exception.InvalidArgumentException;
import kitchenpos.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("메뉴 상품 도메인 테스트")
class MenuProductTest {

    @Test
    @DisplayName("메뉴 상품 필수 체크")
    void validate() {
        assertThatThrownBy(() -> MenuProduct.of(null, 1L))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("상품은 필수입니다.");
    }

    @Test
    @DisplayName("메뉴 상품 동등성 비교")
    void equalMenuProduct() {
        Product 후라이드치킨_상품 = Product.of("후라이드치킨", 10000);
        Product 양념치킨_상품 = Product.of("후라이드치킨", 10000);

        ReflectionTestUtils.setField(후라이드치킨_상품, "id", 1L);
        ReflectionTestUtils.setField(양념치킨_상품, "id", 2L);

        MenuProduct 후라이드치킨 = MenuProduct.of(후라이드치킨_상품, 1L);

        assertAll(
            () -> assertFalse(후라이드치킨.equalMenuProduct(MenuProduct.of(양념치킨_상품, 1L))),
            () -> assertTrue(후라이드치킨.equalMenuProduct(MenuProduct.of(후라이드치킨_상품, 1L)))
        );
    }
}