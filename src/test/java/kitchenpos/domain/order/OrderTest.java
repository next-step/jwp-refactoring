package kitchenpos.domain.order;

import kitchenpos.domain.order.exceptions.InvalidTryOrderException;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {
    @DisplayName("1개 미만의 주문 항목으로 주문 오브젝트를 생성할 수 없다.")
    @Test
    void createFailTest() {
        List<OrderLineItem> emptyOrderLineItems = new ArrayList<>();

        assertThatThrownBy(() -> new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), emptyOrderLineItems))
                .isInstanceOf(InvalidTryOrderException.class)
                .hasMessage("주문하기 위해서는 1개 이상의 주문 항목이 필요합니다.");
    }

    @DisplayName("최초 주문 생성 시 주문 상태는 조리중이며 주문 시간이 존재한다.")
    @Test
    void orderTest() {
        // given
        List<OrderLineItem> orderLineItems = Collections.singletonList(new OrderLineItem(1L, 1L));

        // when
        Order order = new Order(1L, orderLineItems);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(order.getOrderedTime()).isNotNull();
    }
}