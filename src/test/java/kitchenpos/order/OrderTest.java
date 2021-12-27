package kitchenpos.order;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.CannotChangeOrderStatusException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문")
class OrderTest {

    @Test
    @DisplayName("주문 상태 변경 시 주문이 완료 상태면 예외가 발생한다.")
    void changeOrderStatusFailBecauseOfOrderStatusCompletion() {
        // given
        final Order order = new Order(OrderStatus.COMPLETION);

        // when
        assertThatThrownBy(() -> {
            order.changeOrderStatus(OrderStatus.COOKING);
        }).isInstanceOf(CannotChangeOrderStatusException.class);
    }
}
