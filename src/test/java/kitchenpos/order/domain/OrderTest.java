package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTest {
    private Product 후라이드치킨;
    private Product 양념치킨;
    private MenuProduct 후라이드치킨상품;
    private MenuProduct 양념치킨상품;
    private MenuGroup 치킨단품;
    private Menu 후라이드치킨단품;
    private OrderMenu 후라이드치킨단품주문메뉴;
    private OrderMenu 양념치킨단품주문메뉴;
    private Menu 양념치킨단품;
    private OrderLineItem 후라이드치킨단품_주문항목;
    private OrderLineItem 양념치킨단품_주문항목;
    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        후라이드치킨 = Product.of(1L, "치킨버거", BigDecimal.valueOf(15000));
        양념치킨 = Product.of(3L, "불고기버거", BigDecimal.valueOf(16000));
        양념치킨상품 = MenuProduct.of(양념치킨, 1L);
        후라이드치킨상품 = MenuProduct.of(후라이드치킨, 1L);
        치킨단품 = MenuGroup.of(1L, "햄버거단품");
        후라이드치킨단품 = Menu.of(2L, "후라이드치킨단품", BigDecimal.valueOf(15000), 치킨단품.getId(), Collections.singletonList(후라이드치킨상품));
        양념치킨단품 = Menu.of(1L, "양념치킨단품", BigDecimal.valueOf(16000), 치킨단품.getId(), Collections.singletonList(양념치킨상품));
        후라이드치킨단품주문메뉴 = OrderMenu.of(후라이드치킨단품);
        양념치킨단품주문메뉴 = OrderMenu.of(양념치킨단품);
        후라이드치킨단품_주문항목 = OrderLineItem.of(후라이드치킨단품주문메뉴, 2);
        양념치킨단품_주문항목 = OrderLineItem.of(양념치킨단품주문메뉴, 1);
        주문테이블 = OrderTable.of(1L, null, 5, false);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void 주문_생성() {
        // when
        Order order = Order.of(1L, 주문테이블.getId(), OrderLineItems.of(Arrays.asList(후라이드치킨단품_주문항목, 양념치킨단품_주문항목)));

        // then
        assertAll(
                () -> assertThat(order.getOrderTableId()).isEqualTo(주문테이블.getId()),
                () -> assertThat(order.getOrderLineItems().getOrderLineItems().stream().map(OrderLineItem::getMenu))
                        .containsExactly(후라이드치킨단품주문메뉴, 양념치킨단품주문메뉴)
        );
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void 주문_상태변경() {
        // given
        Order order = Order.of(1L, 주문테이블.getId(), OrderLineItems.of(Arrays.asList(후라이드치킨단품_주문항목, 양념치킨단품_주문항목)));

        // when
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @DisplayName("이미 완료된 주문은 상태 변경 시 오류가 발생한다.")
    @Test
    void 완료된주문_상태변경() {
        // given
        Order order = Order.of(1L, 주문테이블.getId(), OrderLineItems.of(Arrays.asList(후라이드치킨단품_주문항목, 양념치킨단품_주문항목)));
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // when & then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("완료되지 않은 주문이면 오류가 발생한다.")
    @Test
    void 완료되지_않은_주문_체크() {
        // given
        Order order = Order.of(1L, 주문테이블.getId(), OrderLineItems.of(Arrays.asList(후라이드치킨단품_주문항목, 양념치킨단품_주문항목)));

        // when & then
        assertThatThrownBy(order::checkOngoingOrderTable)
                .isInstanceOf(IllegalArgumentException.class);
    }
}
