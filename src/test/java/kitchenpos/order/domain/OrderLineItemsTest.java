package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static kitchenpos.application.OrderServiceTest.orderMenu;
import static kitchenpos.application.OrderServiceTest.메뉴상품_생성;
import static org.assertj.core.api.Assertions.assertThat;

class OrderLineItemsTest {
    private MenuGroup 뼈치킨;
    private Menu 뿌링클_세트;
    private Product 뿌링클;
    private Product 치즈볼;
    private OrderTable 주문테이블;
    private Order 주문;
    private OrderLineItem 뿌링클_세트_주문;

    @BeforeEach
    void setUp() {
        뼈치킨 = new MenuGroup("뼈치킨");
        뿌링클_세트 = new Menu("뼈치킨 세트", BigDecimal.valueOf(22000), 뼈치킨);
        뿌링클 = new Product("뿌링클", BigDecimal.valueOf(18000));
        치즈볼 = new Product("치즈볼", BigDecimal.valueOf(4000));
        주문테이블 = new OrderTable(1, false);
        주문 = new Order(주문테이블, OrderStatus.COOKING);

        뿌링클_세트.create(Arrays.asList(메뉴상품_생성(null, 뿌링클, 1L),
                메뉴상품_생성(null, 치즈볼, 2L)));

        뿌링클_세트_주문 = new OrderLineItem(주문, orderMenu(뿌링클_세트.getId(), 뿌링클_세트.getName(), 뿌링클_세트.getPrice()), 1L);
    }

    @DisplayName("주문 상품을 추가한다.")
    @Test
    void 주문_상품_추가() {
        OrderLineItems orderLineItems = new OrderLineItems();

        orderLineItems.addOrderLineItem(주문, 뿌링클_세트_주문);

        assertThat(orderLineItems.getOrderLineItems()).hasSize(1);
    }

    @DisplayName("이미 존재하는 주문 상품은 추가되지 않는다.")
    @Test
    void 이미_존재하는_주문_상품_추가() {
        OrderLineItems orderLineItems = new OrderLineItems();

        orderLineItems.addOrderLineItem(주문, 뿌링클_세트_주문);
        orderLineItems.addOrderLineItem(주문, 뿌링클_세트_주문);

        assertThat(orderLineItems.getOrderLineItems()).hasSize(1);
    }
}
