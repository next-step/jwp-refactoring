package kitchenpos.order.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderLineItemsTest {
    @Test
    @DisplayName("주문 항목 리스트 생성")
    void orderLineItems() {
        // given
        MenuProduct 피자_2판 = MenuProduct.of(1L, Quantity.of(2));
        MenuProducts 피자_구성품 = MenuProducts.of(Arrays.asList(피자_2판));
        MenuGroup 피자_2판_메뉴_그룹 = MenuGroup.of(Name.of("피자_2판_메뉴_그룹"));
        Menu 피자_두판_세트_메뉴 = Menu.of(
                Name.of("피자_두판_세트_메뉴"),
                Price.of(BigDecimal.valueOf(30_000)),
                피자_2판_메뉴_그룹.getId(),
                피자_구성품
        );

        Quantity 수량 = Quantity.of(1);
        OrderLineItem 주문_항목 = OrderLineItem.of(피자_두판_세트_메뉴.getId(), 수량);

        // when
        OrderLineItems 주문_항목_리스트 = OrderLineItems.of(Arrays.asList(주문_항목));

        // then
        assertAll(
                () -> assertThat(주문_항목_리스트).isNotNull(),
                () -> assertThat(주문_항목_리스트.getOrderLineItems()).isEqualTo(Arrays.asList(주문_항목))
        );
    }
}
