package kitchenpos.order.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 항목들 테스트")
class OrderLineItemsTest {

    private Long productId;
    private Long menuGroupId;
    private MenuProducts menuProducts;
    private Menu menu;
    private OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        productId = 1L;
        menuGroupId = 1L;
        menuProducts = MenuProducts.of(Arrays.asList(MenuProduct.of(productId, Quantity.of(2))));
        menu = Menu.of(Name.of("강정치킨_두마리_세트_메뉴"), Price.of(BigDecimal.valueOf(30_000)), menuGroupId, menuProducts);
        orderLineItem = OrderLineItem.of(menu, Quantity.of(1));
    }

    @DisplayName("주문 항목들 생성 성공 테스트")
    @Test
    void instantiate_success() {
        // when
        OrderLineItems orderLineItems = OrderLineItems.of(Arrays.asList(orderLineItem));

        // then
        assertAll(
                () -> assertThat(orderLineItems).isNotNull()
                , () -> assertThat(orderLineItems.getOrderLineItems()).isEqualTo(Arrays.asList(orderLineItem))
        );
    }

    @DisplayName("주문 항목들 생성 실패 테스트")
    @Test
    void instantiate_failure() {
        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> OrderLineItems.of(Collections.emptyList()));
    }
}
