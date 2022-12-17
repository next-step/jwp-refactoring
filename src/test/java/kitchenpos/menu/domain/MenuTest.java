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
}
