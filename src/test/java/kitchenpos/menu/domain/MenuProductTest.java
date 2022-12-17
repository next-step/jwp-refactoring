package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.common.domain.Price;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupTestFixture;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductTestFixture;
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
}
