package kitchenpos.ordertable.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("테이블 검증 테스트")
class OrderTableValidatorTest {

    @Test
    @DisplayName("테이블 상태 변경 시 테이블 그룹 ID가 null이면 예외를 발생한다.")
    void validateEmptyChangableThrowException1() {
        // given
        OrderTableValidator orderTableValidator = new OrderTableValidator(mock(OrderRepository.class));
        Orders orders = new Orders(Collections.singletonList(new Order(OrderStatus.COMPLETION)));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderTableValidator.validateEmptyChangable(1L, orders))
                .withMessageMatching(OrderTableValidator.MESSAGE_VALIDATE_EMPTY_CHANGABLE);
    }

    @Test
    @DisplayName("테이블 상태 변경 시 주문 상태가 계산 완료가 아니면 예외를 발생한다.")
    void validateEmptyChangableThrowException2() {
        // given
        OrderTableValidator orderTableValidator = new OrderTableValidator(mock(OrderRepository.class));
        Orders orders = new Orders(Collections.singletonList(new Order(OrderStatus.MEAL)));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderTableValidator.validateEmptyChangable(null, orders))
                .withMessageMatching(Orders.MESSAGE_VALIDATE_EMPTY_CHANGABLE);
    }

    @Test
    @DisplayName("테이블 그룹 해제 시 주문 상태가 계산 완료가 아니면 예외를 발생한다.")
    void validateOrderTablUngroupableThrowException() {
        // given
        OrderTableValidator orderTableValidator = new OrderTableValidator(mock(OrderRepository.class));
        Orders orders = new Orders(Collections.singletonList(new Order(OrderStatus.MEAL)));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderTableValidator.validateOrderTablUngroupable(orders))
                .withMessageMatching(OrderTableValidator.MESSAGE_VALIDATE_ORDER_TABLE_CHANGABLE);
    }
}
