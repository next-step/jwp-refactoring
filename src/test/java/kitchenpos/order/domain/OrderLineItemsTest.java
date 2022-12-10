package kitchenpos.order.domain;

import static java.util.Collections.singletonList;
import static kitchenpos.menu.domain.MenuGroupTestFixture.generateMenuGroup;
import static kitchenpos.menu.domain.MenuProductTestFixture.generateMenuProduct;
import static kitchenpos.menu.domain.MenuTestFixture.generateMenu;
import static kitchenpos.order.domain.OrderLineItemTestFixture.generateOrderLineItem;
import static kitchenpos.product.domain.ProductTestFixture.generateProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 항목 집합 관련 도메인 테스트")
public class OrderLineItemsTest {

    private Product 치킨버거;
    private Product 불고기버거;
    private MenuProduct 치킨버거상품;
    private MenuProduct 불고기버거상품;
    private MenuGroup 햄버거단품;
    private Menu 불고기버거단품;
    private Menu 치킨버거단품;
    private OrderLineItem 치킨버거단품_주문_항목;
    private OrderLineItem 불고기버거단품_주문_항목;

    @BeforeEach
    void setUp() {
        치킨버거 = generateProduct(2L, "치킨버거", BigDecimal.valueOf(4000L));
        불고기버거 = generateProduct(3L, "불고기버거", BigDecimal.valueOf(3500L));
        불고기버거상품 = generateMenuProduct(불고기버거, 1L);
        치킨버거상품 = generateMenuProduct(치킨버거, 1L);
        햄버거단품 = generateMenuGroup(1L, "햄버거단품");
        치킨버거단품 = generateMenu(2L, "치킨버거단품", BigDecimal.valueOf(4000L), 햄버거단품, singletonList(치킨버거상품));
        불고기버거단품 = generateMenu(1L, "불고기버거단품", BigDecimal.valueOf(3500L), 햄버거단품, singletonList(불고기버거상품));
        치킨버거단품_주문_항목 = generateOrderLineItem(치킨버거단품, 2);
        불고기버거단품_주문_항목 = generateOrderLineItem(불고기버거단품, 1);
    }

    @DisplayName("주문 항목 집합을 생성한다.")
    @Test
    void createOrderLineItems() {
        // when
        OrderLineItems orderLineItems = OrderLineItems.from(Arrays.asList(치킨버거단품_주문_항목, 불고기버거단품_주문_항목));

        // then
        assertAll(
                () -> assertThat(orderLineItems.unmodifiableOrderLineItems()).hasSize(2),
                () -> assertThat(orderLineItems.unmodifiableOrderLineItems()).containsExactly(치킨버거단품_주문_항목, 불고기버거단품_주문_항목)
        );
    }

    @DisplayName("주문 항목이 비어있으면 주문 항목 집합 생성 시 에러가 발생한다.")
    @Test
    void createOrderLineItemsThrowErrorWhenOrderLineItemIsEmpty() {
        // when & then
        assertThatThrownBy(() -> OrderLineItems.from(Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.주문_항목은_비어있을_수_없음.getErrorMessage());
    }
}
