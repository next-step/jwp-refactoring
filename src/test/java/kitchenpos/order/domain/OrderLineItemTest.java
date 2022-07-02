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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderLineItemTest {

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
    @DisplayName("주문 항목 생성 성공 테스트")
    void orderLineItem() {
        // given
        Quantity 수량 = Quantity.of(1);

        // when
        OrderLineItem 주문_항목 = OrderLineItem.of(피자_두판_세트_메뉴, 수량);

        // then
        assertAll(
                () -> assertThat(주문_항목).isNotNull(),
                () -> assertThat(주문_항목.getMenu()).isEqualTo(피자_두판_세트_메뉴),
                () -> assertThat(주문_항목.getQuantity()).isEqualTo(수량.getQuantity())
        );
    }

}
