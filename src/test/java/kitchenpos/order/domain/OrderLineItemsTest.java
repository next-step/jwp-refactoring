package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderLineItemsTest {
    private Product 후라이드치킨;
    private Product 양념치킨;
    private MenuProduct 후라이드치킨상품;
    private MenuProduct 양념치킨상품;
    private MenuGroup 치킨단품;
    private Menu 후라이드치킨단품;
    private Menu 양념치킨단품;
    private OrderLineItem 후라이드치킨단품_주문항목;
    private OrderLineItem 양념치킨단품_주문항목;

    @BeforeEach
    void setUp() {
        후라이드치킨 = Product.of(1L, "치킨버거", BigDecimal.valueOf(15000));
        양념치킨 = Product.of(3L, "불고기버거", BigDecimal.valueOf(16000));
        양념치킨상품 = MenuProduct.of(양념치킨, 1L);
        후라이드치킨상품 = MenuProduct.of(후라이드치킨, 1L);
        치킨단품 = MenuGroup.of(1L, "햄버거단품");
        양념치킨단품 = Menu.of(2L, "치킨버거단품", BigDecimal.valueOf(15000), 치킨단품.getId(), Collections.singletonList(후라이드치킨상품));
        후라이드치킨단품 = Menu.of(1L, "불고기버거단품", BigDecimal.valueOf(16000), 치킨단품.getId(), Collections.singletonList(양념치킨상품));
        후라이드치킨단품_주문항목 = OrderLineItem.of(OrderMenu.of(후라이드치킨단품), 2);
        양념치킨단품_주문항목 = OrderLineItem.of(OrderMenu.of(양념치킨단품), 1);
    }

    @DisplayName("주문 항목 집합을 생성한다.")
    @Test
    void 주문_항목_집합_생성() {
        // when
        OrderLineItems orderLineItems = OrderLineItems.of(Arrays.asList(후라이드치킨단품_주문항목, 양념치킨단품_주문항목));

        // then
        assertAll(
                () -> assertThat(orderLineItems.getOrderLineItems()).hasSize(2),
                () -> assertThat(orderLineItems.getOrderLineItems()).containsExactly(후라이드치킨단품_주문항목, 양념치킨단품_주문항목)
        );
    }

    @DisplayName("주문 항목이 비어있으면 주문 항목 집합 생성 시 에러가 발생한다.")
    @Test
    void 빈_주문항목_주문_항목_집합_생성() {
        // when & then
        assertThatThrownBy(() -> OrderLineItems.of(Collections.emptyList())).isInstanceOf(IllegalArgumentException.class);
    }
}
