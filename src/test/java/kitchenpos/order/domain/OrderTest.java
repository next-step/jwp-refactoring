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
import java.util.Arrays;

import static kitchenpos.common.Messages.ORDER_STATUS_CHANGE_CANNOT_COMPLETION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTest {

    private Product 피자;
    private MenuProduct 피자_2판;
    private MenuProducts 피자_구성품;
    private MenuGroup 피자_2판_메뉴_그룹;
    private OrderTable 주문_테이블_2명;
    private Menu 피자_두판_세트_메뉴;
    private OrderLineItem 주문_항목;
    private OrderLineItems 주문_항목_리스트;

    @BeforeEach
    void setUp() {
        피자 = Product.of(Name.of("피자"), Price.of(BigDecimal.valueOf(17_000)));
        주문_테이블_2명 = OrderTable.of(NumberOfGuests.of(2), Empty.of(false));
        피자_2판_메뉴_그룹 = MenuGroup.of(Name.of("피자_2판_메뉴_그룹"));
        피자_2판 = MenuProduct.of(피자, Quantity.of(2));
        피자_구성품 = MenuProducts.of(Arrays.asList(피자_2판));
        피자_두판_세트_메뉴 = Menu.of(Name.of("피자_두판_세트_메뉴"), Price.of(BigDecimal.valueOf(30_000)), 피자_2판_메뉴_그룹, 피자_구성품);

        주문_항목 = OrderLineItem.of(피자_두판_세트_메뉴, Quantity.of(1));
        주문_항목_리스트 = OrderLineItems.of(Arrays.asList(주문_항목));
    }

    @Test
    @DisplayName("주문 생성")
    void order() {
        // when
        Order 주문 = Order.of(1L, 주문_테이블_2명, OrderStatus.COOKING, 주문_항목_리스트);

        // then
        assertAll(
                () -> assertThat(주문).isNotNull(),
                () -> assertThat(주문.getOrderTable()).isEqualTo(주문_테이블_2명),
                () -> assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                () -> assertThat(주문.getOrderLineItems()).size().isEqualTo(1),
                () -> assertThat(주문.getOrderLineItems()).isEqualTo(주문_항목_리스트.getOrderLineItems())
        );
    }

    @Test
    @DisplayName("주문 생성시 주문테이블이 비어있는 경우 실패")
    void validateOrderTable() {
        OrderTable 비어있는_주문테이블 = OrderTable.of(NumberOfGuests.of(2), Empty.of(true));

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Order.of(1L, 비어있는_주문테이블, OrderStatus.COOKING, 주문_항목_리스트));
    }

    @Test
    @DisplayName("주문 상태 수정")
    void changeOrderStatus() {
        // given
        Order 주문 = Order.of(1L, 주문_테이블_2명, OrderStatus.COOKING, 주문_항목_리스트);

        // when
        주문.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertAll(
                () -> assertThat(주문).isNotNull(),
                () -> assertThat(주문.getOrderTable()).isEqualTo(주문_테이블_2명),
                () -> assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.MEAL),
                () -> assertThat(주문.getOrderLineItems()).size().isEqualTo(1),
                () -> assertThat(주문.getOrderLineItems()).isEqualTo(주문_항목_리스트.getOrderLineItems())
        );
    }

    @Test
    @DisplayName("주문 상태 수정시 완료건인 경우 실패")
    void changeOrderCompletion() {
        // given
        Order 주문 = Order.of(1L, 주문_테이블_2명, OrderStatus.COMPLETION, 주문_항목_리스트);

        // when
        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> 주문.changeOrderStatus(OrderStatus.MEAL))
                .withMessage(ORDER_STATUS_CHANGE_CANNOT_COMPLETION)
        ;
    }
}
