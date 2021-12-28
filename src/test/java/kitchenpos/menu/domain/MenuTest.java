package kitchenpos.menu.domain;

import kitchenpos.exception.InvalidPriceException;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuTest {
    @DisplayName("가격이 없거나 음수인 경우에는 메뉴를 등록할 수 없다.")
    @Test
    void create() {
        assertAll(
                () -> assertThatThrownBy(
                        () -> Menu.of(1L,"후라이드치킨", -1000L, null, null)
                ).isInstanceOf(InvalidPriceException.class),
                () -> assertThatThrownBy(
                        () -> Menu.of(1L,"후라이드치킨", null, null, null)
                ).isInstanceOf(InvalidPriceException.class)
        );
    }

    @DisplayName("메뉴상품 목록의 총 금액의 합보다 메뉴 금액이 비싼 경우 등록할 수 없다.")
    @Test
    void create2() {
        // given
        Product product = Product.of(1L, "후라이드치킨", 16000L);
        Product product2 = Product.of(2L, "양념치킨", 16000L);
        MenuProduct menuProduct = MenuProduct.of(1L, null, product, 1);
        MenuProduct menuProduct2 = MenuProduct.of(2L, null, product2, 1);

        //then
        assertThatThrownBy(
                () -> Menu.of("후라이드치킨", 33000L, null, Arrays.asList(menuProduct, menuProduct2))
        ).isInstanceOf(InvalidPriceException.class);
    }
}
