package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
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
        OrderMenu orderMenu = new OrderMenu(1L, "Test", BigDecimal.ONE);
        OrderLineItem orderLineItem = new OrderLineItem(orderMenu, 10);
        Order order = new Order(10L, Collections.singletonList(orderLineItem));

        assertThat(order.getOrderTableId()).isEqualTo(10L);
        assertThat(order.getOrderedTime()).isNotNull();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(order.getOrderLineItems()).hasSize(1);
    }

    @ParameterizedTest(name = "{0}, {1} -> {2}")
    @DisplayName("Order 생성시 실페 케이스를 체크한다.")
    @MethodSource("providerCreateOrderFailCase")
    void createOrderFail(Long orderTableId, List<OrderLineItem> orderLineItems, Class<? extends Exception> exception) {
        assertThatExceptionOfType(exception)
            .isThrownBy(() -> new Order(orderTableId, orderLineItems));
    }

    private static Stream<Arguments> providerCreateOrderFailCase() {
        OrderMenu orderMenu = new OrderMenu(1L, "Test", BigDecimal.ONE);
        OrderLineItem orderLineItem = new OrderLineItem(orderMenu, 10);

        return Stream.of(
            Arguments.of(null, Collections.singletonList(orderLineItem), NullPointerException.class),
            Arguments.of(10L, Collections.emptyList(), IllegalArgumentException.class)
        );
    }

    @Test
    @DisplayName("주문의 상태를 변경한다.")
    void changeOrderStatus() {
        OrderMenu orderMenu = new OrderMenu(1L, "Test", BigDecimal.ONE);
        OrderLineItem orderLineItem = new OrderLineItem(orderMenu, 10);
        Order order = new Order(10L, Collections.singletonList(orderLineItem));
        order.changeOrderStatus(OrderStatus.COMPLETION);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @Test
    @DisplayName("이미 완료된 주문은 상태를 변경할 수 없다.")
    void changeOrderStatusFail() {
        OrderMenu orderMenu = new OrderMenu(1L, "Test", BigDecimal.ONE);
        OrderLineItem orderLineItem = new OrderLineItem(orderMenu, 10);
        Order order = new Order(10L, Collections.singletonList(orderLineItem));
        order.changeOrderStatus(OrderStatus.COMPLETION);

        assertThatIllegalArgumentException()
            .isThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL));
    }

}
