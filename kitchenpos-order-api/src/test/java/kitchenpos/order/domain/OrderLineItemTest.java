package kitchenpos.order.domain;

import static java.util.Collections.singletonList;
import static kitchenpos.menugroup.domain.MenuGroupTestFixture.generateMenuGroup;
import static kitchenpos.menu.domain.MenuProductTestFixture.generateMenuProduct;
import static kitchenpos.menu.domain.MenuTestFixture.generateMenu;
import static kitchenpos.order.domain.OrderLineItemTestFixture.generateOrderLineItem;
import static kitchenpos.order.domain.OrderMenuTestFixture.generateOrderMenu;
import static kitchenpos.product.domain.ProductTestFixture.generateProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProductTestFixture;
import kitchenpos.menu.domain.MenuTestFixture;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroupTestFixture;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductTestFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 항목 관련 도메인 테스트")
public class OrderLineItemTest {

    private Product 치킨버거;
    private MenuProduct 치킨버거상품;
    private MenuGroup 햄버거단품;
    private Menu 치킨버거단품;
    private OrderMenu 치킨버거단품주문메뉴;

    @BeforeEach
    void setUp() {
        치킨버거 = ProductTestFixture.generateProduct(2L, "치킨버거", BigDecimal.valueOf(4000L));
        치킨버거상품 = MenuProductTestFixture.generateMenuProduct(치킨버거, 1L);
        햄버거단품 = MenuGroupTestFixture.generateMenuGroup(1L, "햄버거단품");
        치킨버거단품 = MenuTestFixture.generateMenu(2L, "치킨버거단품", BigDecimal.valueOf(4000L), 햄버거단품, singletonList(치킨버거상품));
        치킨버거단품주문메뉴 = generateOrderMenu(치킨버거단품);
    }

    @DisplayName("주문 항목을 생성한다.")
    @Test
    void createOrderLineItem() {
        // when
        OrderLineItem 치킨버거단품_주문_항목 = generateOrderLineItem(치킨버거단품주문메뉴, 2);

        // then
        assertAll(
                () -> Assertions.assertThat(치킨버거단품_주문_항목.getOrderMenu()).isEqualTo(치킨버거단품주문메뉴),
                () -> Assertions.assertThat(치킨버거단품_주문_항목.getQuantity()).isEqualTo(Quantity.from(2))
        );
    }
}
