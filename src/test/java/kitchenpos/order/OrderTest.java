package kitchenpos.order;

import static kitchenpos.order.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.exception.AtLeastOneOrderLineItemException;
import kitchenpos.order.exception.CannotChangeOrderStatusException;

class OrderTest {

    @DisplayName("[주문 등록] 빈 주문항목목록으로 주문을 등록할 수 없다")
    @Test
    void test1() {
        assertThatThrownBy(() -> new Order(1L, Collections.emptyList()))
            .isInstanceOf(AtLeastOneOrderLineItemException.class);
    }

    @DisplayName("[주문상태 변경] 주문상태가 완료인 경우 상태변경할 수 없다")
    @Test
    void test3() {
        Order order = new Order(1L, Collections.singletonList(new OrderLineItem()));
        order.changeStatus(COMPLETION);
        assertThatThrownBy(() -> {
            order.changeStatus(COOKING);
        })
            .isInstanceOf(CannotChangeOrderStatusException.class);
    }

}
