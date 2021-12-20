package kitchenpos.menu.domain;

import kitchenpos.fixture.ProductFixture;
import kitchenpos.product.domain.Product;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.menuGroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuTest {

    @DisplayName("메뉴에 메뉴 상품 추가")
    @Test
    void addMenuProducts() {
        Product 후라이드 = ProductFixture.생성("후라이드", new BigDecimal("5000"));
        MenuGroup 치킨류 = MenuGroupFixture.생성(1L, "치킨");
        MenuProduct 후라이드두마리구성 = MenuProductFixture.생성(1L, 후라이드, 2L);
        Menu 후라이드두마리세트 = MenuFixture.생성(1L, "후라이드두마리세트", new BigDecimal("10000"), 치킨류);

        후라이드두마리세트.addMenuProducts(Arrays.asList(후라이드두마리구성));

        assertThat(후라이드두마리세트.getMenuProducts()).isEqualTo(Arrays.asList(후라이드두마리구성));
    }
}
