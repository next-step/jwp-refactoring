package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.message.OrderMessage;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    private OrderTable orderTable;
    private List<OrderLineItem> orderLineItems;


    @BeforeEach
    void setUp() {
        this.orderTable = OrderTable.of(1, false);

        MenuGroup menuGroup = new MenuGroup("한가지 메뉴");
        MenuProduct menuProduct = MenuProduct.of(1L, 1L);
        MenuProduct menuProduct2 = MenuProduct.of(2L, 1L);
        Menu menu1 = Menu.of("후라이드치킨", 16_000L, 1L, Arrays.asList(menuProduct));
        Menu menu2 = Menu.of("강정치킨", 12_000L, 1L, Arrays.asList(menuProduct2));
        this.orderLineItems = Arrays.asList(
                OrderLineItem.of(menu1, 1L),
                OrderLineItem.of(menu2, 1L)
        );
    }

    @Test
    @DisplayName("주문 생성에 성공한다")
    void createOrderTest() {
        // when
        Order order = new Order(orderTable, orderLineItems);

        // then
        assertThat(order).isEqualTo(new Order(orderTable, orderLineItems));
    }

    @Test
    @DisplayName("주문 생성시 주문 테이블이 빈테이블인 경우 생성에 실패한다")
    void createOrderThrownByEmptyOrderTableTest() {
        // given
        orderTable.changeEmpty(true);

        // when & then
        assertThatThrownBy(() -> new Order(orderTable, orderLineItems))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderMessage.CREATE_ERROR_ORDER_TABLE_IS_EMPTY.message());
    }

    @Test
    @DisplayName("주문 생성시 주문 상품이 비어있는 경우 생성에 실패한다")
    void createOrderThrownByEmptyOrderItemsTest() {
        // given
        orderLineItems = new ArrayList<>();

        // when & then
        assertThatThrownBy(() -> new Order(orderTable, orderLineItems))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderMessage.CREATE_ERROR_ORDER_LINE_ITEMS_IS_EMPTY.message());
    }

    @Test
    @DisplayName("주문의 상태가 조리중 또는 식사중 상태인 경우 [true]를 반환한다")
    void isCookingOrMealStateTest() {
        // given
        Order order = Order.cooking(orderTable, orderLineItems);

        // when
        boolean orderState = order.isCookingOrMealState();

        // then
        assertThat(orderState).isTrue();
    }

    @Test
    @DisplayName("주문의 상태를 변경한다")
    void changeOrderStateTest() {
        // given
        Order order = Order.cooking(orderTable, orderLineItems);

        // when
        order.changeState(OrderStatus.COMPLETION);

        // then
        assertThat(order.isCookingOrMealState()).isFalse();
    }
}
