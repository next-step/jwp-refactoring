package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderLineItemTest {
    private Product 후라이드치킨;
    private MenuProduct 치킨상품;
    private MenuGroup 치킨단품;
    private Menu 후라이드치킨단품;

    @BeforeEach
    void setUp() {
        후라이드치킨 = Product.of(1L, "후라이드치킨", 18_000L);
        치킨상품 = MenuProduct.of(후라이드치킨, 1L);
        치킨단품 = MenuGroup.of(1L, "치킨단품");
        후라이드치킨단품 = Menu.of(1L, "후라이드치킨단품", BigDecimal.valueOf(15_000L), 치킨단품.getId(), Collections.singletonList(치킨상품));
    }

    @Test
    void 주문_생성() {
        OrderMenu orderMenu = OrderMenu.of(후라이드치킨단품);
        OrderLineItem orderLineItem = OrderLineItem.of(orderMenu, 1);

        assertAll(
                () -> assertThat(orderLineItem.getMenu()).isEqualTo(orderMenu),
                () -> assertThat(orderLineItem.getQuantity()).isEqualTo(1)
        );
    }
}
