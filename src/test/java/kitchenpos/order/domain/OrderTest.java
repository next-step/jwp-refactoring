package kitchenpos.order.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.Empty;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 테스트")
class OrderTest {

    private OrderTable orderTable;
    private LocalDateTime orderedTime;
    private Long productId;
    private MenuProducts menuProducts;
    private Menu menu;
    private Long menuGroupId;
    private OrderLineItem orderLineItem;
    private OrderLineItems orderLineItems;

    @BeforeEach
    void setUp() {
        orderTable = OrderTable.of(NumberOfGuests.of(4), Empty.of(false));
        orderedTime = LocalDateTime.now();
        productId = 1L;
        menuProducts = MenuProducts.of(Arrays.asList(MenuProduct.of(productId, Quantity.of(2))));
        menuGroupId = 1L;
        menu = Menu.of(Name.of("강정치킨_두마리_세트_메뉴"), Price.of(BigDecimal.valueOf(30_000)), menuGroupId, menuProducts);
        orderLineItem = OrderLineItem.of(menu, Quantity.of(1));
        orderLineItems = OrderLineItems.of(Arrays.asList(orderLineItem));
    }

    @DisplayName("주문 생성 성공 테스트")
    @Test
    void instantiate_success() {
        // when
        Order order = Order.of(orderTable, OrderStatus.COOKING, orderedTime, orderLineItems);

        // then
        assertAll(
                () -> assertThat(order).isNotNull()
                , () -> assertThat(order.getOrderTable()).isEqualTo(orderTable)
                , () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING)
                , () -> assertThat(order.getOrderedTime()).isEqualTo(orderedTime)
                , () -> assertThat(order.getOrderLineItems()).isEqualTo(orderLineItems)
        );
    }

    @DisplayName("주문 상태 수정 성공 테스트")
    @Test
    void changeOrderStatus_success() {
        // given
        Order order = Order.of(orderTable, OrderStatus.COOKING, orderedTime, orderLineItems);

        // when
        order.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertAll(
                () -> assertThat(order).isNotNull()
                , () -> assertThat(order.getOrderTable()).isEqualTo(orderTable)
                , () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL)
                , () -> assertThat(order.getOrderedTime()).isEqualTo(orderedTime)
                , () -> assertThat(order.getOrderLineItems()).isEqualTo(orderLineItems)
        );
    }

    @DisplayName("주문 상태 수정 실패 테스트 - 주문 상태가 completion인 경우 주문 상태 변경할 수 없")
    @Test
    void changeOrderStatus_failure() {
        // given
        Order order = Order.of(orderTable, OrderStatus.COMPLETION, orderedTime, orderLineItems);

        // when & then
        org.assertj.core.api.Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL));
    }
}
