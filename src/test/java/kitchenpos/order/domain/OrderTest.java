package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.testfixture.CommonTestFixture.createOrder;
import static kitchenpos.testfixture.CommonTestFixture.createOrderLineItem;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {
    @Test
    @DisplayName("테이블 id가 존재해야 한다.")
    void validateOrder() {
        // then
        assertThatThrownBy(() -> {
            createOrder(null, Collections.emptyList());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문상태 변경 시 이미 완료된 주문이 아니어야 한다.")
    void changeOrderStatus() {
        // given
        List<OrderLineItem> orderLineItems = Arrays.asList(
                createOrderLineItem(new OrderMenu(1L, "싱글세트", BigDecimal.valueOf(15_000)), 1L),
                createOrderLineItem(new OrderMenu(1L, "더블세트", BigDecimal.valueOf(25_000)), 1L)
        );
        Order order = createOrder(1L, OrderStatus.COMPLETION, orderLineItems);

        // then
        assertThatThrownBy(() -> {
            order.changeOrderStatus(OrderStatus.COMPLETION);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
