package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class OrderTest {

    @Test
    @DisplayName("주문 항목이 없으면 생성에 실패한다.")
    void constructor_order_line_items_empty() {
        // given
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        OrderTable orderTable = OrderTable.of(4, false);

        // when, then
        assertThatThrownBy(() -> Order.of(orderLineItems, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 항목이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("주문 테이블이 빈 테이블이면 생성에 실패한다.")
    void constructor_order_table_empty() {
        // given
        MenuGroup menuGroup = MenuGroup.of("두마리메뉴");
        Product product = Product.of("후라이드", new BigDecimal(16_000));

        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(MenuProduct.of(product, 1));
        Menu menu = Menu.of("후라이드치킨", new BigDecimal(16_000), menuGroup, menuProducts);

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(OrderLineItem.of(menu, 1));
        OrderTable orderTable = OrderTable.of(4, true);

        // when, then
        assertThatThrownBy(() -> Order.of(orderLineItems, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 테이블에는 주문을 등록할 수 없습니다.");
    }

    @ParameterizedTest
    @CsvSource(value = {"COOKING", "MEAL"})
    @DisplayName("주문 상태를 변경합니다.")
    void changeOrderStatus(OrderStatus orderStatus) {
        // given
        MenuGroup menuGroup = MenuGroup.of("두마리메뉴");

        Product product = Product.of("후라이드", new BigDecimal(16_000));
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(MenuProduct.of(product, 1));

        Menu menu = Menu.of("후라이드치킨", new BigDecimal(16_000), menuGroup, menuProducts);

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(OrderLineItem.of(menu, 1));

        OrderTable orderTable = OrderTable.of(4, false);

        Order order = Order.of(orderLineItems, orderTable, orderStatus);

        // when
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // then
        assertThatNoException();
    }

    @Test
    @DisplayName("이미 계산 완료된 주문이면 주문 상태를 변경할 수 없다.")
    void changeOrderStatus_complete() {
        // given
        MenuGroup menuGroup = MenuGroup.of("두마리메뉴");

        Product product = Product.of("후라이드", new BigDecimal(16_000));
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(MenuProduct.of(product, 1));

        Menu menu = Menu.of("후라이드치킨", new BigDecimal(16_000), menuGroup, menuProducts);

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(OrderLineItem.of(menu, 1));

        OrderTable orderTable = OrderTable.of(4, false);

        Order order = Order.of(orderLineItems, orderTable, OrderStatus.COMPLETION);

        // when, then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COMPLETION))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 계산 완료된 주문입니다.");
    }

    @ParameterizedTest
    @CsvSource(value = {"COOKING:false", "MEAL:true", "COMPLETION:false"}, delimiter = ':')
    @DisplayName("주문 상태가 같은지 확인합니다.")
    void isEqualToOrderStatus(OrderStatus orderStatus, boolean expect) {
        // given
        MenuGroup menuGroup = MenuGroup.of("두마리메뉴");

        Product product = Product.of("후라이드", new BigDecimal(16_000));
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(MenuProduct.of(product, 1));

        Menu menu = Menu.of("후라이드치킨", new BigDecimal(16_000), menuGroup, menuProducts);

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(OrderLineItem.of(menu, 1));

        OrderTable orderTable = OrderTable.of(4, false);

        Order order = Order.of(orderLineItems, orderTable, OrderStatus.MEAL);

        // when
        boolean result = order.isEqualToOrderStatus(orderStatus);

        // then
        assertThat(result).isEqualTo(expect);
    }
}