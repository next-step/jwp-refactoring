package kitchenpos.tablegroup.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.common.domain.Quantity;
import kitchenpos.common.error.ErrorEnum;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.product.domain.Product;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TableGroupTest {
    private OrderTables orderTables;
    private OrderTable firstTable;
    private OrderTable secondTable;
    TableGroup tableGroup;
        @BeforeEach
        void setUp() {
            tableGroup = TableGroup.of(1L);
            firstTable = new OrderTable(1L, new NumberOfGuests(0), true);
            secondTable = new OrderTable(2L, new NumberOfGuests(0), true);
            orderTables = OrderTables.of(Arrays.asList(firstTable, secondTable));
            orderTables.group(tableGroup.getId());
        }

    @Test
    void 단체_지정된_테이블을_해제할_수_있다() {
        // given
        Order order1 = createOrder(OrderStatus.COMPLETION);
        Order order2 = createOrder(OrderStatus.COMPLETION);
        OrderTable firstOrderTable = new OrderTable(new NumberOfGuests(4), false);
        OrderTable secondOrderTable = new OrderTable(new NumberOfGuests(4), false);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        // when
        tableGroup.ungroup(Arrays.asList(order1, order2));

        // then
        assertAll(
                () -> assertThat(firstOrderTable.getTableGroupId()).isNull(),
                () -> assertThat(secondOrderTable.getTableGroupId()).isNull()
        );
    }

    @Test
    void 단체_지정된_테이블을_해제할_때_주문상태가_결제완료가_아니면_예외를_발생한다() {
        // given
        Order firstOrder = createOrder(OrderStatus.MEAL);
        Order secondOrder = createOrder(OrderStatus.MEAL);
        TableGroup tableGroup = TableGroup.of(1L);

        // when & then
        assertThatThrownBy(() -> tableGroup.ungroup(Arrays.asList(firstOrder, secondOrder)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.NOT_PAYMENT_ORDER.message());
    }

    public static Order createOrder(OrderStatus orderStatus) {
        Product 양념치킨 = new Product(1L, "양념치킨", 20_000L);
        Product 스파게티 = new Product(2L, "스파게티", 10_000L);
        MenuProduct 치킨_두마리 = new MenuProduct(1L, new Quantity(2L), null, 양념치킨);
        MenuProduct 스파게티_이인분 = new MenuProduct(2L, new Quantity(2L), null, 스파게티);
        List<MenuProduct> menuProducts = Arrays.asList(치킨_두마리, 스파게티_이인분);

        Menu 치킨_스파게티_더블세트_메뉴 = Menu.of(1L, "치킨 스파게티 더블세트 메뉴", BigDecimal.valueOf(13_000L), 1L, menuProducts);
        OrderLineItem 주문_항목 = OrderLineItem.of(OrderMenu.of(치킨_스파게티_더블세트_메뉴), 1L);

        OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(4), false);
        Order order = Order.of(orderTable.getId(), new OrderLineItems(Arrays.asList(주문_항목)));
        order.setOrderStatus(orderStatus);
        orderTable.updateEmpty(true);
        return order;
    }
}

