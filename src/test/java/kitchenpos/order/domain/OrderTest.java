package kitchenpos.order.domain;

import kitchenpos.common.domain.Quantity;
import kitchenpos.common.exception.IllegalArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DisplayName("주문 도메인 테스트")
public class OrderTest {
    @DisplayName("완료된 주문의 상태 변경 요청 예외")
    @Test
    void 완료된_주문_상태_변경_요청_검증() {
        Order order = Order.of(1L, Collections.singletonList(OrderLineItem.of(1L, Quantity.of(1L))));
        order.changeOrderStatus(OrderStatus.COMPLETION);

        Throwable thrown = catchThrowable(() -> order.changeOrderStatus(OrderStatus.COOKING));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료된 주문의 상태는 변경할 수 없습니다.");
    }
}
