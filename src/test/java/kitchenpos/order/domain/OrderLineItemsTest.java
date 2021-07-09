package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {

    @DisplayName("제네릭을 사용한 변환 테스트")
    @Test
    void convertAllTest() {
        // given
        Product 불고기 = new Product("불고기", BigDecimal.valueOf(1000L));
        MenuGroup 메뉴_그룹 = new MenuGroup("메뉴 그룹");
        Menu 메뉴 = Menu.Builder.of("메뉴1", BigDecimal.valueOf(2000L))
                              .menuGroup(메뉴_그룹)
                              .menuProducts(Arrays.asList(new MenuProduct(불고기, 5)))
                              .build();
        OrderLineItems orderLineItems = new OrderLineItems();
        OrderLineItem 주문아이템1 = new OrderLineItem(메뉴, 3);
        OrderLineItem 주문아이템2 = new OrderLineItem(메뉴, 5);
        orderLineItems.add(Arrays.asList(주문아이템1, 주문아이템2));

        // when
        assertThat(orderLineItems.convertAll(orderLineItem -> orderLineItem.getQuantity()))
            .hasSize(2)
            .containsExactly(3L, 5L);
    }

}
