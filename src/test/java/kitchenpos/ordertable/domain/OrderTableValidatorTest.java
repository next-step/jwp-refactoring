package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ExceptionMessage;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
class OrderTableValidatorTest {

    @Mock
    private OrderRepository orderRepository;

    private OrderTableValidator orderTableValidator;

    private List<OrderStatus> notCompletionOrderStatuses;

    @BeforeEach
    void setUp() {
        orderTableValidator = new OrderTableValidator(orderRepository);
        notCompletionOrderStatuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
    }

    @DisplayName("단체 지정이 되어있지 않고 계산 완료인 주문 테이블은 empty 를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        // given
        OrderTable orderTable = OrderTable.of(1L, null, 0, true);
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
            notCompletionOrderStatuses)).willReturn(false);

        // when && then
        assertDoesNotThrow(() -> orderTableValidator.validateChangeEmpty(orderTable));
    }

    @DisplayName("단체 지정이 되어있고 계산 완료 이전인 주문 테이블은 empty 를 변경할 수 없다.")
    @Test
    void changeEmptyException() {
        // given
        OrderTable existGroupOrderTable = OrderTable.of(1L, 1L, 0, true);
        OrderTable existNotCompletionStatusOrderTable = OrderTable.of(2L, null, 0, true);
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(existNotCompletionStatusOrderTable.getId(),
            notCompletionOrderStatuses)).willReturn(true);

        // when && then
        assertAll(
            () -> assertThatThrownBy(() ->
                orderTableValidator.validateChangeEmpty(existGroupOrderTable))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.CANNOT_CHANGE_STATUS.getMessage()),
            () -> assertThatThrownBy(() ->
                orderTableValidator.validateChangeEmpty(existNotCompletionStatusOrderTable))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.CANNOT_CHANGE_STATUS.getMessage())
        );
    }
}
