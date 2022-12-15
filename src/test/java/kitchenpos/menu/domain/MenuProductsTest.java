package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.common.domain.Price;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {

    private Product 하와이안피자;
    private Product 콜라;
    private MenuGroup 피자;
    private MenuProduct 하와이안피자상품;
    private MenuProduct 콜라상품;

    @BeforeEach
    void setUp() {
        하와이안피자 = new Product("하와이안피자", BigDecimal.valueOf(15_000));
        콜라 = new Product("하와이안피자", BigDecimal.valueOf(1_000));
        피자 = new MenuGroup("피자");
        하와이안피자상품 = new MenuProduct(하와이안피자, 1);
        콜라상품 = new MenuProduct(콜라, 1);
    }

    @DisplayName("메뉴 상품이 빈 값이면 에러가 발생한다.")
    @Test
    void validateMenuProductsNotEmptyException() {
        assertThatThrownBy(() -> MenuProducts.from(Collections.emptyList()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 메뉴 상품의 총 가격의 합을 가져온다.")
    @Test
    void totalPrice() {
        MenuProducts menuProducts = MenuProducts.from(Arrays.asList(하와이안피자상품, 콜라상품));

        Price result = menuProducts.totalPrice();

        assertThat(result.value()).isEqualTo(BigDecimal.valueOf(16_000));
    }
}
