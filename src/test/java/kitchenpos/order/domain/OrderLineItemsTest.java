package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 상품 목록을 괸리하는 클래스 테스트")
class OrderLineItemsTest {
    private MenuGroup 양식;
    private Menu 양식_세트;
    private Product 스파게티;
    private Product 에이드;
    private OrderTable 주문테이블;
    private Order 주문;
    private OrderLineItem 양식_세트_주문;

    @BeforeEach
    void setUp() {
        양식 = new MenuGroup("양식");
        양식_세트 = new Menu("양식 세트", new BigDecimal(22000), 양식);
        스파게티 = new Product("스파게티", new BigDecimal(18000));
        에이드 = new Product("에이드", new BigDecimal(4000));
        주문테이블 = new OrderTable(1, false);
        주문 = new Order(주문테이블, OrderStatus.COOKING);

        양식_세트.create(Arrays.asList(new MenuProduct(양식_세트, 스파게티, 1L),
                new MenuProduct(양식_세트, 에이드, 2L)));

        양식_세트_주문 = new OrderLineItem(주문, 양식_세트, 1L);
    }

    @Test
    void 주문_상품_추가() {
        OrderLineItems orderLineItems = new OrderLineItems();

        orderLineItems.addOrderLineItem(주문, 양식_세트_주문);

        assertThat(orderLineItems.getOrderLineItems()).hasSize(1);
    }

    @Test
    void 이미_존재하는_주문_상품은_추가되지_않음() {
        OrderLineItems orderLineItems = new OrderLineItems();

        orderLineItems.addOrderLineItem(주문, 양식_세트_주문);
        orderLineItems.addOrderLineItem(주문, 양식_세트_주문);

        assertThat(orderLineItems.getOrderLineItems()).hasSize(1);
    }
}
