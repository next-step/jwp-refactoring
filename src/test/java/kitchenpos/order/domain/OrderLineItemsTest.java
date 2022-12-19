package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 상품 목록 단위 테스트")
class OrderLineItemsTest {
    private OrderTable 우아한_주문_테이블_1;
    private Order 첫번째_주문;
    private Product 짬뽕;
    private Product 탕수육;
    private MenuGroup 중식;
    private Menu 중식_세트;
    private OrderLineItem 중식_세트_주문;

    @BeforeEach
    void setUp() {
        우아한_주문_테이블_1 = new OrderTable(1, false);
        첫번째_주문 = new Order(우아한_주문_테이블_1, OrderStatus.COOKING);
        짬뽕 = new Product("짬뽕", 8000);
        탕수육 = new Product("탕수육", 18000);
        중식 = new MenuGroup("중식");
        중식_세트 = new Menu("중식 세트", 26000, 중식);
        중식_세트_주문 = new OrderLineItem(첫번째_주문, 중식_세트, 1L);
        중식_세트.create(Arrays.asList(
                new MenuProduct(중식_세트, 짬뽕, 1L),
                new MenuProduct(중식_세트, 탕수육, 1L)));
    }

    @DisplayName("주문 상품을 추가한다.")
    @Test
    void 주문_상품을_추가한다() {
        OrderLineItems orderLineItems = new OrderLineItems();

        orderLineItems.addOrderLineItem(첫번째_주문, 중식_세트_주문);

        assertThat(orderLineItems.values()).hasSize(1);
    }

    @DisplayName("이미 존재하는 주문 상품은 추가되지 않는다.")
    @Test
    void 이미_존재하는_주문_상품은_추가되지_않는다() {
        OrderLineItems orderLineItems = new OrderLineItems();

        orderLineItems.addOrderLineItem(첫번째_주문, 중식_세트_주문);
        orderLineItems.addOrderLineItem(첫번째_주문, 중식_세트_주문);

        assertThat(orderLineItems.values()).hasSize(1);
    }
}
