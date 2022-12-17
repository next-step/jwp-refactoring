package kitchenpos.menu.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    private Product 하와이안피자;
    private MenuGroup 피자;
    private MenuProduct 하와이안피자상품;

    @BeforeEach
    void setUp() {
        하와이안피자 = new Product("하와이안피자", BigDecimal.valueOf(15_000));
        피자 = new MenuGroup("피자");
        하와이안피자상품 = new MenuProduct(하와이안피자, 1);
    }

    @DisplayName("메뉴 가격이 모든 상품 가격의 합보다 크면 에러가 발생한다.")
    @Test
    void validatePriceException() {
        assertThatThrownBy(() -> new Menu("하와이안피자세트", BigDecimal.valueOf(18_000), 피자.getId(),
            MenuProducts.from(Arrays.asList(하와이안피자상품))))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
