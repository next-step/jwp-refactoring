package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class OrderTest {

    @Test
    @DisplayName("Order의 정상 생성을 확인한다.")
    void createOrder() {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 10);
        Order order = new Order(10L, Collections.singletonList(orderLineItem));

        assertThat(order.getOrderTableId()).isEqualTo(10L);
        assertThat(order.getOrderedTime()).isNotNull();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(order.getOrderLineItems()).hasSize(1);
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Order 생성시 실페 케이스를 체크한다.")
    @MethodSource("providerCreateOrderFailCase")
    void createOrderFail(String testName, Long orderTableId, List<OrderLineItem> orderLineItems) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new Order(orderTableId, orderLineItems));
    }

    private static Stream<Arguments> providerCreateOrderFailCase() {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 10);

        return Stream.of(
            Arguments.of("테이블이 존재하지 않을 경우", null, Collections.singletonList(orderLineItem)),
            Arguments.of("주문 메뉴가 존재하지 않았을 경우", 10L, Collections.emptyList())
        );
    }

    @Test
    @DisplayName("주문의 상태를 변경한다.")
    void changeOrderStatus() {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 10);
        Order order = new Order(10L, Collections.singletonList(orderLineItem));
        order.changeOrderStatus(OrderStatus.COMPLETION);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @Test
    @DisplayName("이미 완료된 주문은 상태를 변경할 수 없다.")
    void changeOrderStatusFail() {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 10);
        Order order = new Order(10L, Collections.singletonList(orderLineItem));
        order.changeOrderStatus(OrderStatus.COMPLETION);

        assertThatIllegalArgumentException()
            .isThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL));
    }

}
