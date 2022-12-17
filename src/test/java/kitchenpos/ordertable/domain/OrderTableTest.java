package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductTestFixture;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupTestFixture;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderMenuTestFixture;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductTestFixture;
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
    private OrderMenu 주문메뉴;

    @BeforeEach
    void setUp() {
        하와이안피자 = ProductTestFixture.create("하와이안피자", BigDecimal.valueOf(15_000));
        피자 = MenuGroupTestFixture.create("피자");
        하와이안피자상품 = MenuProductTestFixture.create(하와이안피자, 1);
        하와이안피자세트 = Menu.of("하와이안피자세트", BigDecimal.valueOf(15_000L), 피자.getId(),
            MenuProducts.from(Arrays.asList(하와이안피자상품)));
        주문메뉴 = OrderMenuTestFixture.create(하와이안피자세트);
        하와이안피자세트주문 = OrderLineItem.of(주문메뉴, 1);
    }

    @DisplayName("주문 테이블의 비어있는 상태를 수정한다.")
    @Test
    void updateEmpty() {
        OrderTable orderTable = OrderTable.of(4, true);

        orderTable.changeEmpty(false, Collections.emptyList());

        assertThat(orderTable.isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블의 단체 지정을 해제한다.")
    @Test
    void ungroup() {
        OrderTable orderTable1 = OrderTable.of(4, false);
        OrderTable orderTable2 = OrderTable.of(6, true);
        TableGroup tableGroup = TableGroup.from(1L);
        OrderTables.from(Arrays.asList(orderTable1, orderTable2)).registerTableGroup(tableGroup.getId());

        orderTable2.ungroup();

        assertAll(
            () -> assertThat(orderTable1.findTableGroupId()).isNotNull(),
            () -> assertThat(orderTable1.isNotNullTableGroup()).isTrue(),
            () -> assertThat(orderTable2.findTableGroupId()).isNull(),
            () -> assertThat(orderTable2.isNotNullTableGroup()).isFalse()
        );
    }

    @DisplayName("주문 테이블의 손님 수를 수정한다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = OrderTable.of(4, false);

        orderTable.changeNumberOfGuests(6);

        assertThat(orderTable.getNumberOfGuests().value()).isEqualTo(6);
    }

    @DisplayName("비어있는 상태의 주문 테이블의 손님 수를 수정하면 에러가 발생한다.")
    @Test
    void validateOrderTableNotEmptyException() {
        OrderTable orderTable = OrderTable.of(4, true);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(6))
            .isInstanceOf(IllegalArgumentException.class);
    }
}