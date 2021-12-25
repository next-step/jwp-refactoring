package kitchenpos.order;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문")
class OrderTest {

    @Test
    @DisplayName("주문 상태 변경 시 주문이 완료 상태면 예외가 발생한다.")
    void changeOrderStatusFailBecauseOfOrderStatusCompletion() {
        // given
        final Order order = Order.builder()
                .orderStatus(OrderStatus.COMPLETION)
                .build();

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            order.changeOrderStatus(OrderStatus.COOKING);
        });
    }
}
