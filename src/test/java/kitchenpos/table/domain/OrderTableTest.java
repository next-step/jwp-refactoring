package kitchenpos.table.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @Test
    @DisplayName("주문 테이블을 빈 테이블로 변경한다.")
    void empty() {
        // given
        OrderTable orderTable = OrderTable.of(4, false);

        // when
        orderTable.empty();

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("이미 단체로 지정되어 있으면 빈 테이블로 변경할 수 없다.")
    void empty_exist_table_group() {
        // given
        OrderTable orderTable1 = OrderTable.of(4, true);
        OrderTable orderTable2 = OrderTable.of(4, true);

        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);

        TableGroup tableGroup = TableGroup.of(orderTables);
        orderTable1.applyTableGroup(tableGroup);

        // when, then
        assertThatThrownBy(() -> orderTable1.empty())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 단체로 지정되어 있어서 빈 테이블로 변경할 수 없습니다.");
    }

    @ParameterizedTest
    @CsvSource(value = {"COOKING", "MEAL"})
    @DisplayName("조리 또는 식사 중이면 빈 테이블로 변경할 수 없다.")
    void empty_order_status_fail(OrderStatus orderStatus) {
        // given
        MenuGroup menuGroup = MenuGroup.of("두마리메뉴");

        Product product = Product.of("후라이드", new BigDecimal(16_000));
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(MenuProduct.of(product, 1));

        Menu menu = Menu.of("후라이드치킨", new BigDecimal(16_000), menuGroup, menuProducts);

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(OrderLineItem.of(menu, 1));

        Order order = Order.of(orderLineItems, OrderTable.of(4, false), orderStatus);

        OrderTable orderTable = OrderTable.of(4, false, order);

        // when, then
        assertThatThrownBy(() -> orderTable.empty())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("조리 또는 식사 중이면 빈 테이블로 변경할 수 없습니다.");
    }

    @Test
    @DisplayName("방문한 손님 수를 변경합니다.")
    void changeNumberOfGuests() {
        // given
        OrderTable orderTable = OrderTable.of(2, false);
        int numberOfGuests = 4;

        // when
        orderTable.changeNumberOfGuests(numberOfGuests);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @Test
    @DisplayName("변경할 방문 손님 수가 음수이면 손님 수를 변경할 수 없다.")
    void changeNumberOfGuests_negative() {
        // given
        OrderTable orderTable = OrderTable.of(2, false);

        // when, then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("변경할 방문 손님수가 0보다 작습니다.");
    }

    @Test
    @DisplayName("빈 테이블이면 손님 수를 변경할 수 없다.")
    void changeNumberOfGuests_empty() {
        // given
        OrderTable orderTable = OrderTable.of(2, true);

        // when, then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 주문 테이블은 빈 테이블입니다.");
    }

    @Test
    @DisplayName("단체를 지정합니다.")
    void applyTableGroup() {
        // given
        OrderTable orderTable = OrderTable.of(2, true);
        TableGroup tableGroup = new TableGroup();

        // when
        orderTable.applyTableGroup(tableGroup);

        // then
        assertThat(orderTable.getTableGroup()).isNotNull();
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("단체 지정을 해제합니다.")
    void ungroup() {
        // given
        MenuGroup menuGroup = MenuGroup.of("두마리메뉴");

        Product product = Product.of("후라이드", new BigDecimal(16_000));
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(MenuProduct.of(product, 1));

        Menu menu = Menu.of("후라이드치킨", new BigDecimal(16_000), menuGroup, menuProducts);

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(OrderLineItem.of(menu, 1));

        Order order = Order.of(orderLineItems, OrderTable.of(4, false), OrderStatus.COMPLETION);

        OrderTable orderTable = OrderTable.of(4, false, order);

        TableGroup tableGroup = new TableGroup();
        orderTable.applyTableGroup(tableGroup);

        // when
        orderTable.ungroup();

        // then
        assertThat(orderTable.getTableGroup()).isNull();
    }

    @ParameterizedTest
    @CsvSource(value = {"COOKING", "MEAL"})
    @DisplayName("조리 또는 식사 중이면 단체 지정을 해제할 수 없습니다.")
    void ungroup_order_status(OrderStatus orderStatus) {
        // given
        MenuGroup menuGroup = MenuGroup.of("두마리메뉴");

        Product product = Product.of("후라이드", new BigDecimal(16_000));
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(MenuProduct.of(product, 1));

        Menu menu = Menu.of("후라이드치킨", new BigDecimal(16_000), menuGroup, menuProducts);

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(OrderLineItem.of(menu, 1));

        Order order = Order.of(orderLineItems, OrderTable.of(4, false), orderStatus);

        OrderTable orderTable = OrderTable.of(4, false, order);

        TableGroup tableGroup = new TableGroup();
        orderTable.applyTableGroup(tableGroup);

        // when, then
        assertThatThrownBy(() -> orderTable.ungroup())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("조리 또는 식사 중인 주문 테이블이 존재합니다.");
    }
}