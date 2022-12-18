package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.menu.testfixture.MenuProductTestFixture;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.testfixture.MenuGroupTestFixture;
import kitchenpos.product.domain.Product;
import kitchenpos.product.testfixture.ProductTestFixture;
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
        하와이안피자 = ProductTestFixture.create("하와이안피자", BigDecimal.valueOf(15_000));
        콜라 = ProductTestFixture.create("하와이안피자", BigDecimal.valueOf(1_000));
        피자 = MenuGroupTestFixture.create("피자");
        하와이안피자상품 = MenuProductTestFixture.create(하와이안피자, 1);
        콜라상품 = MenuProductTestFixture.create(콜라, 1);
    }

    @DisplayName("메뉴 상품이 빈 값이면 에러가 발생한다.")
    @Test
    void validateMenuProductsNotEmptyException() {
        assertThatThrownBy(() -> MenuProducts.from(Collections.emptyList()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
