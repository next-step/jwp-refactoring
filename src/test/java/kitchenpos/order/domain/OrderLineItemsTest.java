package kitchenpos.order.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.common.Messages.ORDER_LINE_ITEM_IDS_FIND_IN_NO_SUCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderLineItemsTest {

    private Product 피자;
    private MenuProduct 피자_2판;
    private MenuProducts 피자_구성품;
    private MenuGroup 피자_2판_메뉴_그룹;
    private Menu 피자_두판_세트_메뉴;

    @BeforeEach
    void setUp() {
        피자 = Product.of(Name.of("피자"), Price.of(BigDecimal.valueOf(17_000)));
        피자_2판 = MenuProduct.of(피자, Quantity.of(2));
        피자_구성품 = MenuProducts.of(Arrays.asList(피자_2판));
        피자_2판_메뉴_그룹 = MenuGroup.of(Name.of("피자_2판_메뉴_그룹"));
        피자_두판_세트_메뉴 = Menu.of(Name.of("피자_두판_세트_메뉴"), Price.of(BigDecimal.valueOf(30_000)), 피자_2판_메뉴_그룹, 피자_구성품);
    }

    @Test
    @DisplayName("주문 항목 리스트 생성")
    void orderLineItems() {
        // given
        Quantity 수량 = Quantity.of(1);
        OrderLineItem 주문_항목 = OrderLineItem.of(피자_두판_세트_메뉴, 수량);

        // when
        OrderLineItems 주문_항목_리스트 = OrderLineItems.of(Arrays.asList(주문_항목));

        // then
        assertAll(
                () -> assertThat(주문_항목_리스트).isNotNull(),
                () -> assertThat(주문_항목_리스트.getOrderLineItems()).isEqualTo(Arrays.asList(주문_항목))
        );
    }

    @Test
    @DisplayName("주문 항목 리스트 생성시 ")
    void validateOrderLineItems() {
        // given
        OrderLineItem 주문_항목 = OrderLineItem.of(피자_두판_세트_메뉴, Quantity.of(1));
        OrderLineItem 주문_항목_2개 = OrderLineItem.of(피자_두판_세트_메뉴, Quantity.of(2));
        OrderLineItems 주문_항목_리스트 = OrderLineItems.of(Arrays.asList(주문_항목));
        List<OrderLineItem> requestOrderLineItems = Arrays.asList(주문_항목, 주문_항목_2개);

        // when
        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> 주문_항목_리스트.validateOrderLineItems(requestOrderLineItems))
                .withMessage(ORDER_LINE_ITEM_IDS_FIND_IN_NO_SUCH)
        ;
    }
}
