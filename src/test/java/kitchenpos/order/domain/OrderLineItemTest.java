package kitchenpos.order.domain;

import static kitchenpos.product.ProductFixture.강정치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemTest {

    MenuProduct 더블강정치킨상품;
    MenuGroup 추천메뉴;
    Menu 더블강정치킨;

    @BeforeEach
    void setUp() {
        추천메뉴 = new MenuGroup(1L, "추천메뉴");
        더블강정치킨상품 = new MenuProduct(1L, null, 강정치킨, 2L);
        더블강정치킨 = new Menu(1L, "더블강정치킨", new BigDecimal(19_000), 추천메뉴,
            Collections.singletonList(더블강정치킨상품));
    }

    @Test
    @DisplayName("주문 항목 생성")
    void createOrderLineItem() {
        //when
        OrderLineItem orderLineItem = new OrderLineItem(null, null, 더블강정치킨.convertToOrderMenu(),
            3L);

        //then
        assertThat(orderLineItem.getQuantity()).isEqualTo(3L);
        assertThat(orderLineItem.menuId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("주문 항목 생성시 메뉴가 없으면 에러 발생")
    void noMenuException() {
        //when & then
        assertThatThrownBy(() -> new OrderLineItem(null, null, null, 3L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주문 항목에 메뉴가 없습니다.");
    }
}
