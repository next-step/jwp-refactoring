package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTableValidatorTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderTableValidator orderTableValidator;

    @DisplayName("완료되지 않은 주문이 존재하는 경우 예외가 발생한다.")
    @Test
    void existNotCompleteOrderException() {
        given(orderRepository.findAllByOrderTableId(1L)).willReturn(Arrays.asList(new Order(1L)));

        assertThatThrownBy(() -> orderTableValidator.validateOrderStatus(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료되지 않은 주문이 존재합니다.");
    }
}
