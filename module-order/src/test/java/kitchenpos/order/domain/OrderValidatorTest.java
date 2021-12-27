package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import kitchenpos.common.exception.CannotUpdatedException;
import kitchenpos.common.exception.InvalidArgumentException;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 정합성 체크 테스트")
@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderValidator orderValidator;

    final OrderLineItem orderLineItem = OrderLineItem.of(
        OrderMenu.of(1L, "후라이드치킨", BigDecimal.valueOf(10000)), 2L);

    @Test
    @DisplayName("주문 생성 validate 체크: 테이블 정보는 필수, 빈 테이블인 경우 주문을 생성할 수 없다.")
    void validateCreateOrder() {


        assertThatThrownBy(() -> orderValidator.validateCreateOrder(null, Arrays.asList(orderLineItem)))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("해당하는 테이블이 없습니다.");

        when(orderTableRepository.findById(anyLong()))
            .thenReturn(Optional.of(OrderTable.of(0, true)));

        assertThatThrownBy(() -> orderValidator.validateCreateOrder(1L,  Arrays.asList(orderLineItem)))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("빈 테이블은 주문을 할 수 없습니다.");
    }

    @Test
    @DisplayName("주문 상태 변경")
    void updateOrderStatus() {
        Order order = Order.of(1L, null, Arrays.asList(orderLineItem));
        assertTrue(order.isOnGoing());

        order.updateOrderStatus(OrderStatus.COMPLETION);

        assertFalse(order.isOnGoing());

        assertThatThrownBy(() -> orderValidator.validateUpdateOrderStatus(order))
            .isInstanceOf(CannotUpdatedException.class)
            .hasMessage("계산완료된 주문은 변경할 수 없습니다.");
    }
}