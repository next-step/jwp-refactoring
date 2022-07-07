package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTableOrderStatusValidatorImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderTableOrderStatusValidatorImpl tableOrderStatusValidator;

    @Test
    @DisplayName("조리중이거나 식사중인 주문 테이블이 포함된 경우 예외 발생 검증")

    public void throwException_WhenOrderTablesOrderHasMealStatusOrCookingStatus() {
        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        List<OrderStatus> canNotChangeOrderTableStatuses = OrderStatus.canNotChangeOrderTableStatuses();

        // Given
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, canNotChangeOrderTableStatuses
        )).willReturn(true);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tableOrderStatusValidator.validateOrderStatus(orderTableIds));

        verify(orderRepository)
            .existsByOrderTableIdInAndOrderStatusIn(orderTableIds, canNotChangeOrderTableStatuses);
    }
}
