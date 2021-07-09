package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @DisplayName("주문 항목이 올바르지 않으면 등록할 수 없다 : 주문 항목은 1개 이상이어야 한다.")
    @Test
    void createTest_emptyOrderLineItem() {
        // when & then
        assertThatThrownBy(() -> new Order(1L, new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 상태가 올바르지 않으면 변경할 수 없다 : 주문의 상태가 ('요리중', '식사중') 이어야 한다.")
    @Test
    void changeOrderStatusTest_orderStatusCompletion() {
        // given
        Order order = new Order(1L, 1L);
        order.changeOrderStatus(OrderStatus.COMPLETION.name());

        // when & then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL.name()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
