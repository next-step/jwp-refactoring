package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {
    @DisplayName("주문항목 목록에 할당된 메뉴 ID를 가져올 수 있다.")
    @Test
    void assignedMenus() {
        OrderLineItems given = new OrderLineItems();
        given.add(new OrderLineItem(1L, 10));
        given.add(new OrderLineItem(2L, 10));

        List<Long> actual = given.assignedMenu();

        assertThat(actual).contains(1L, 2L);
    }
}
