package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.testfixture.MenuProductTestFixture;
import kitchenpos.menu.testfixture.MenuTestFixture;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.testfixture.MenuGroupTestFixture;
import kitchenpos.product.domain.Product;
import kitchenpos.product.testfixture.ProductTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    private Product 하와이안피자;
    private MenuGroup 피자;
    private MenuProduct 하와이안피자상품;

    @BeforeEach
    void setUp() {
        하와이안피자 = ProductTestFixture.create("하와이안피자", BigDecimal.valueOf(15_000));
        피자 = MenuGroupTestFixture.create("피자");
        하와이안피자상품 = MenuProductTestFixture.create(하와이안피자, 1);
    }

    @DisplayName("메뉴를 지정한다.")
    @Test
    void assignMenu() {
        Menu menu = MenuTestFixture.create("하와이안피자세트", java.math.BigDecimal.valueOf(15_000), 피자, Arrays.asList(하와이안피자상품));

        하와이안피자상품.assignMenu(menu);

        assertThat(하와이안피자상품.getMenu()).isEqualTo(menu);
    }
}
