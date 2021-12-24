package kitchenpos.menu.domain;

import kitchenpos.menu.application.exception.InvalidPrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 테스트")
class MenuTest {

    private MenuGroup 인기메뉴그룹;
    private Product 통통치킨한마리;
    private MenuProduct 메뉴상품;

    @BeforeEach
    void setUp() {
        인기메뉴그룹 = new MenuGroup("인기메뉴그룹");
        통통치킨한마리 = new Product("통통치킨한마리", BigDecimal.valueOf(10000));
        메뉴상품 = new MenuProduct(통통치킨한마리, 1L);
    }

    @Test
    @DisplayName("메뉴의 가격이 상품 가격의 합보다 큰 경우 예외가 발생한다.")
    void validateProductsSum() {
        BigDecimal 더비싼가격 = BigDecimal.valueOf(12000);

        assertThatThrownBy(() -> Menu.of("통통치킨한마리", 더비싼가격, 인기메뉴그룹, Collections.singletonList(메뉴상품)))
                .isInstanceOf(InvalidPrice.class);
    }
}
