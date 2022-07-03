package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import kitchenpos.common.exception.CannotUpdateException;
import kitchenpos.common.exception.ExceptionType;
import kitchenpos.order.application.OrderValidator;
import kitchenpos.order.dto.request.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문에 대한 단위 테스트")
class OrderTest {
    private OrderRequest 주문_요청;
    private OrderValidator orderValidator;

    @BeforeEach
    void setUp() {
        orderValidator = mock(OrderValidator.class);
        주문_요청 = new OrderRequest(1L, null, null, Collections.emptyList());
    }

    @DisplayName("주문을 생성하면 정상적으로 조리중 상태, 주문생성시간이 생성된다")
    @Test
    void create_test() {
        // when
        Order result = Order.of(orderValidator, 주문_요청);

        // then
        assertNotNull(result.getOrderedTime());
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @DisplayName("주문의 상태를 변경하면 정상적으로 변경된다")
    @Test
    void change_status_test() {
        // given
        Order 주문 = Order.of(orderValidator, 주문_요청);

        // when
        주문.changeOrderStatus(OrderStatus.COMPLETION);

        // then
        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @DisplayName("주문상태 변경시 이미 완료된 상태면 예외가 발생한다")
    @Test
    void order_status_exception_test() {
        // given
        Order 주문 = Order.of(orderValidator, 주문_요청);
        주문.changeOrderStatus(OrderStatus.COMPLETION);

        // then
        assertThatThrownBy(() -> 주문.changeOrderStatus(OrderStatus.COMPLETION))
            .isInstanceOf(CannotUpdateException.class)
            .hasMessageContaining(ExceptionType.COMPLETION_STATUS_CAN_NOT_CHANGE.getMessage());
    }
}
