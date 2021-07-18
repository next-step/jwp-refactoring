package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.OrderStatus;
import kitchenpos.common.exception.CannotChangeOrderStatusException;

class OrderTest {
    @Test
    @DisplayName("주문 완료상태인 주문의 주문상태 수정 시 오류 발생")
    void name() {
        // given
        Order order = new Order(LocalDateTime.now(), 1L);
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(CannotChangeOrderStatusException.class)
                .hasMessage("주문상태가 COMPLETION 인 건은 상태수정이 불가능합니다.");
    }
}
