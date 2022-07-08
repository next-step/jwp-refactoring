package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.exception.IllegalOrderException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static fixture.MenuFixtureFactory.createMenu;
import static fixture.MenuGroupFixtureFactory.createMenuGroup;
import static fixture.MenuProductFixtureFactory.createMenuProduct;
import static fixture.OrderTableFixtureFactory.createOrderTable;
import static fixture.ProductFixtureFactory.createProduct;
import static kitchenpos.utils.fixture.OrderFixtureFactory.createOrder;
import static kitchenpos.utils.fixture.OrderLineItemFixtureFactory.createOrderLineItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("주문 도메인 테스트")
class OrderTest {
    private MenuGroup menuGroup_한식;
    private Product product_김치찌개;
    private Menu menu;
    private MenuProduct menuProduct_김치찌개;


    @BeforeEach
    public void setUp(){
        menuGroup_한식 = createMenuGroup(1L, "한식");
        product_김치찌개 = createProduct(1L, "김치찌개", 8000);
        menuProduct_김치찌개 = createMenuProduct(product_김치찌개.getId(), 1);
        menu = createMenu(1L, "김치찌개", 8000, menuGroup_한식.getId(), Arrays.asList(menuProduct_김치찌개));
    }

    @DisplayName("주문을 생성한다")
    @Test
    void Order_생성() {
        OrderTable 테이블_1 = createOrderTable(1L, null, 3, false);
        OrderLineItem orderLineItem_김치찌개 = createOrderLineItem(menu.getId(), 1);
        Order order = createOrder(테이블_1.getId(), Arrays.asList(orderLineItem_김치찌개));

        assertAll(
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                () -> assertThat(order.getOrderLineItems()).containsExactly(orderLineItem_김치찌개),
                () -> assertThat(orderLineItem_김치찌개.getOrder()).isEqualTo(order)
        );
    }

    @DisplayName("주문의 상태를 변경할 수 있다")
    @Test
    void Order_주문상태_변경(){
        OrderTable 테이블_1 = createOrderTable(1L, null, 3, false);
        OrderLineItem orderLineItem_김치찌개 = createOrderLineItem(menu.getId(), 1);
        Order order = createOrder(테이블_1.getId(), Arrays.asList(orderLineItem_김치찌개));
        order.changeStatus(OrderStatus.COMPLETION);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @DisplayName("주문의 상태가 COMPLETION이면 주문의 상태를 변경할 수 있다")
    @Test
    void Order_주문상태_변경_COMPLETION_검증(){
        OrderTable 테이블_1 = createOrderTable(1L, null, 3, false);
        OrderLineItem orderLineItem_김치찌개 = createOrderLineItem(menu.getId(), 1);
        Order order = createOrder(테이블_1.getId(), Arrays.asList(orderLineItem_김치찌개));
        order.changeStatus(OrderStatus.COMPLETION);

        assertThrows(IllegalOrderException.class, () -> order.changeStatus(OrderStatus.COOKING));
    }
}