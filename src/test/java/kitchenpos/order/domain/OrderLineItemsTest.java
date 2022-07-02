package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.menu.domain.MenuTest.두마리치킨_메뉴;
import static kitchenpos.menu.domain.MenuTest.불고기치즈버거_메뉴;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class OrderLineItemsTest {

    @DisplayName("주문 상품 생성한다.")
    @Test
    void createOrder() {
        new OrderLineItems(주문(), Arrays.asList(두마리치킨_메뉴_주문상품(), 불고기치즈버거_메뉴_주문상품()));
    }

    @DisplayName("주문 상품 없이는 주문을 할 수 없다.")
    @Test
    void createOrderWithEmptyOrderLineItems() {
        // when, then
        assertThatThrownBy(() -> {
            new OrderLineItems(주문(), Collections.EMPTY_LIST);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("동일한 메뉴를 여러 개의 주문 상품으로 주문할 수 없다.")
    @Test
    void createOrderSameMenuDuplicateMenuLineItem() {
        // when, then
        assertThatThrownBy(() -> {
            new OrderLineItems(주문(), Arrays.asList(두마리치킨_메뉴_주문상품(), 두마리치킨_메뉴_주문상품()));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    public static OrderLineItem 두마리치킨_메뉴_주문상품() {
        return new OrderLineItem(두마리치킨_메뉴().getId(), 1);
    }

    public static OrderLineItem 불고기치즈버거_메뉴_주문상품() {
        return new OrderLineItem(불고기치즈버거_메뉴().getId(), 1);
    }

    public static List<OrderLineItem> 주문상품_리스트() {
        return Arrays.asList(두마리치킨_메뉴_주문상품(), 불고기치즈버거_메뉴_주문상품());
    }

    public static Order 주문() {
        return new Order(1L);
    }


}
