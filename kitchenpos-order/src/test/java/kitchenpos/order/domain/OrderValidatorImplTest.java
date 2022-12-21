package kitchenpos.order.domain;

import kitchenpos.order.message.OrderMessage;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OrderValidatorImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderValidatorImpl orderValidator;

    @Test
    @DisplayName("주어진 주문 테이블 아이디 목록중 조리 또는 식사인 상태의 주문이 있으면 예외처리 한다")
    void validateChangeEmptyTest() {
        // given
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);
        List<Long> orderTableIds = Arrays.asList(1L, 2L);

        // then
        AssertionsForClassTypes.assertThatThrownBy(() -> orderValidator.validateOrderStatusIsCookingOrMealByTableIds(orderTableIds))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderMessage.ERROR_INVALID_ORDER_STATE.message());

        // then
        then(orderRepository).should(times(1)).existsByOrderTableIdInAndOrderStatusIn(any(), any());
    }
}
