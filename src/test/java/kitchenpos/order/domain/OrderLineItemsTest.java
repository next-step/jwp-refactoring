package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.common.Price;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {

    @DisplayName("제네릭을 사용한 변환 테스트")
    @Test
    void convertAllTest() {
        // given
        Product 불고기 = new Product("불고기", Price.of(BigDecimal.valueOf(1000L)));
        MenuGroup 메뉴_그룹 = new MenuGroup("메뉴 그룹");
        OrderLineItems orderLineItems = new OrderLineItems();
        OrderLineItem 주문아이템1 = new OrderLineItem(1L, 3);
        OrderLineItem 주문아이템2 = new OrderLineItem(2L, 5);
        orderLineItems.add(Arrays.asList(주문아이템1, 주문아이템2));

        // when
        assertThat(orderLineItems.convertAll(orderLineItem -> orderLineItem.getQuantity()))
            .hasSize(2)
            .containsExactly(3L, 5L);
    }

}
