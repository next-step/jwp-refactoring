package kitchenpos.menu.domain;

import kitchenpos.common.fixtrue.MenuGroupFixture;
import kitchenpos.common.fixtrue.ProductFixture;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("메뉴 상품들 테스트")
class MenuProductsTest {

    MenuGroup 두마리치킨;
    Product 후라이드치킨;

    @BeforeEach
    void setUp() {
        후라이드치킨 = ProductFixture.of("후라이드치킨", BigDecimal.valueOf(16000));
        두마리치킨 = MenuGroupFixture.from("두마리치킨");
    }

    @Test
    void 메뉴_상품들_생성() {
        MenuProducts actual = MenuProducts.of(Collections.singletonList(MenuProduct.of(후라이드치킨, 2L)));

        Assertions.assertThat(actual).isNotNull();
    }
}
