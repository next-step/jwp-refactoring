package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.product.domain.Product;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    private Product 하와이안피자;
    private MenuGroup 피자;
    private MenuProduct 하와이안피자상품;
    private Menu 하와이안피자세트;
    private OrderLineItem 하와이안피자세트주문;

    @BeforeEach
    void setUp() {
        하와이안피자 = new Product("하와이안피자", BigDecimal.valueOf(15_000));
        피자 = new MenuGroup("피자");
        하와이안피자상품 = new MenuProduct(하와이안피자, 1);
        하와이안피자세트 = new Menu("하와이안피자세트", BigDecimal.valueOf(15_000L), 피자,
            MenuProducts.from(Arrays.asList(하와이안피자상품)));
        하와이안피자세트주문 = new OrderLineItem(하와이안피자세트, 1);
    }

    @DisplayName("주문 테이블의 비어있는 상태를 수정한다.")
    @Test
    void updateEmpty() {
        OrderTable orderTable = new OrderTable(4, true);

        orderTable.changeEmpty(false, Collections.emptyList());

        assertThat(orderTable.isEmpty()).isFalse();
    }

    @DisplayName("완료되지 않은 주문이 있으면 주문 테이블의 비어있는 상태를 수정 시 에러가 발생한다.")
    @Test
    void validateHasTableGroupException() {
        OrderTable orderTable = new OrderTable(4, false);
        Order order = Order.of(orderTable, OrderLineItems.from(Collections.singletonList(하와이안피자세트주문)));

        assertThatThrownBy(() -> orderTable.changeEmpty(true, Collections.singletonList(order)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 단체 지정을 해제한다.")
    @Test
    void ungroup() {
        OrderTable orderTable1 = new OrderTable(4, false);
        OrderTable orderTable2 = new OrderTable(6, true);
        TableGroup tableGroup = new TableGroup(OrderTables.from(Arrays.asList(orderTable1, orderTable2)));

        orderTable2.ungroup();

        assertAll(
            () -> assertThat(orderTable1.getTableGroup()).isNotNull(),
            () -> assertThat(orderTable1.isNotNullTableGroup()).isTrue(),
            () -> assertThat(orderTable2.getTableGroup()).isNull(),
            () -> assertThat(orderTable2.isNotNullTableGroup()).isFalse()
        );
    }

    @DisplayName("주문 테이블의 손님 수를 수정한다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = new OrderTable(4, false);

        orderTable.changeNumberOfGuests(6);

        assertThat(orderTable.getNumberOfGuests().value()).isEqualTo(6);
    }

    @DisplayName("비어있는 상태의 주문 테이블의 손님 수를 수정하면 에러가 발생한다.")
    @Test
    void validateOrderTableNotEmptyException() {
        OrderTable orderTable = new OrderTable(4, true);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(6))
            .isInstanceOf(IllegalArgumentException.class);
    }
}