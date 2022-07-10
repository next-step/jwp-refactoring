package kichenpos.order.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static kichenpos.order.application.OrderServiceTest.orderedTime;
import static kichenpos.order.domain.OrderLineItemsTest.createOrderLineItems;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTest {

    @Test
    void 주문을_처리한다() {
        // given
        LocalDateTime orderedTime = orderedTime();

        // when
        Order order = new Order(1L, createOrderLineItems().elements(), orderedTime);

        // then
        assertAll(
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                () -> assertThat(order.getOrderedTime()).isEqualTo(orderedTime)
        );
    }

    @Test
    void 계산_완료한_주문은_상태를_변경할_수_없다() {
        // given
        Order order = new Order(OrderStatus.COMPLETION);

        // when & then
        assertThatThrownBy(() ->
                order.changeOrderStatus(OrderStatus.COOKING)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("계산 완료한 주문은 상태를 변경할 수 없습니다.");
    }
}
