package kitchenpos.order.domain;

import kitchenpos.common.fixtrue.MenuFixture;
import kitchenpos.common.fixtrue.MenuGroupFixture;
import kitchenpos.common.fixtrue.MenuProductFixture;
import kitchenpos.common.fixtrue.ProductFixture;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 항목들 테스트")
class OrderLineItemsTest {

    Menu 후라이드_후라이드;

    @BeforeEach
    void setUp() {
        Product 후라이드치킨 = ProductFixture.of("후라이드치킨", BigDecimal.valueOf(16000));
        MenuGroup 두마리치킨 = MenuGroupFixture.from("두마리치킨");
        후라이드_후라이드 = MenuFixture.of(
                "후라이드+후라이드",
                BigDecimal.valueOf(31000),
                두마리치킨);

        후라이드_후라이드.addMenuProduct(Collections.singletonList(MenuProductFixture.of(후라이드치킨, 2)));
    }

    @Test
    void 주문_항목들_생성() {
        OrderLineItems actual = OrderLineItems.from(Collections.singletonList(OrderLineItem.of(후라이드_후라이드, 1L)));

        assertThat(actual).isNotNull();
    }
}
