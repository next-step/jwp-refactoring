package kitchenpos.order.domain;

import static kitchenpos.utils.DomainFixtureFactory.createMenu;
import static kitchenpos.utils.DomainFixtureFactory.createMenuGroup;
import static kitchenpos.utils.DomainFixtureFactory.createMenuProduct;
import static kitchenpos.utils.DomainFixtureFactory.createOrderLineItem;
import static kitchenpos.utils.DomainFixtureFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.util.ArrayList;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {
    OrderLineItem 주문항목;

    @BeforeEach
    void setUp() {
        Product 양념 = createProduct(1L, "양념", BigDecimal.valueOf(20000L));
        MenuGroup 한마리메뉴 = createMenuGroup(1L, "한마리메뉴");
        MenuProduct 양념치킨상품 = createMenuProduct(양념.id(), 2L);
        Menu 양념치킨 = createMenu(1L, "양념치킨", BigDecimal.valueOf(40000L), 한마리메뉴.id(),
                MenuProducts.from(Lists.newArrayList(양념치킨상품)));
        주문항목 = createOrderLineItem(OrderMenu.from(양념치킨), 2L);
    }

    @DisplayName("초기화 테스트")
    @Test
    void from() {
        OrderLineItems orderLineItems = OrderLineItems.from(Lists.newArrayList(주문항목));
        assertThat(orderLineItems.readOnlyOrderLineItems()).isEqualTo(Lists.newArrayList(주문항목));
    }

    @DisplayName("null 경우 테스트")
    @Test
    void ofWithNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> OrderLineItems.from(null)).withMessage("주문 항목이 필요합니다.");
    }

    @DisplayName("빈 경우 테스트")
    @Test
    void ofWithEmpty() {
        assertThatIllegalArgumentException().isThrownBy(() -> OrderLineItems.from(new ArrayList<>()))
                .withMessage("주문 항목이 필요합니다.");
    }
}
