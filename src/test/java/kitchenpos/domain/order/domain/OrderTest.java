package kitchenpos.domain.order.domain;

import kitchenpos.domain.order.domain.Order;
import kitchenpos.domain.order.domain.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class OrderTest {

    @Test
    void checkCompleteOrder_완료_상태의_주문이면_에러를_발생한다() {
        Order order = new Order(OrderStatus.COMPLETION);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> order.checkCompleteOrder());
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class)
    void changeOrderStatus_주문_상태를_변경한다(OrderStatus orderStatus) {
        Order order = new Order(OrderStatus.COMPLETION);
        order.changeOrderStatus(orderStatus);
        assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
    }
}
