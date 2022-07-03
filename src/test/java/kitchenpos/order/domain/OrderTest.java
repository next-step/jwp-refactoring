package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.testfixture.CommonTestFixture.createOrder;
import static kitchenpos.testfixture.CommonTestFixture.createOrderLineItem;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {
    @Test
    @DisplayName("테이블 id가 존재하지 않으면 않으면 Exception 발생 확인")
    void validateOrder() {
        // then
        assertThatThrownBy(() -> {
            createOrder(null, Collections.emptyList());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문상태 변경 시 이미 완료된 주문이면 Exception 발생 확인")
    void changeOrderStatus() {
        // given
        List<OrderLineItem> orderLineItems = Arrays.asList(
                createOrderLineItem(1L, 1L),
                createOrderLineItem(2L, 1L)
        );
        Order order = createOrder(1L, OrderStatus.COMPLETION, orderLineItems);

        // then
        assertThatThrownBy(() -> {
            order.changeOrderStatus(OrderStatus.COMPLETION);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
