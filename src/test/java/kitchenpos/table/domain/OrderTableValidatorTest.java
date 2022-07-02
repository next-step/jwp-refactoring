package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTableValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("주문 테이블 유효성검사 관련")
@SpringBootTest
class OrderTableValidatorTest {
    @Autowired
    OrderTableValidator orderTableValidator;
    @MockBean
    OrderRepository orderRepository;

    @DisplayName("등록된 주문의 주문 상태가 계산 완료인 경우에만 변경할 수 있다")
    @Test
    void checkOrderStatus() {
        // given
        Long orderTableId = 1L;
        when(orderRepository.findAllByOrderTableId(orderTableId)).thenReturn(singletonList(new Order(orderTableId, OrderStatus.MEAL)));

        // when then
        assertThatThrownBy(() -> orderTableValidator.checkOrderStatus(orderTableId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 주문의 주문 상태가 계산 완료인 경우에만 변경할 수 있다")
    @Test
    void checkOrderStatusIn() {
        // given
        Long orderTableId = 1L;
        List<Long> orderTableIds = singletonList(orderTableId);
        when(orderRepository.findAllByOrderTableIdIn(orderTableIds)).thenReturn(singletonList(new Order(orderTableId, OrderStatus.MEAL)));

        // when then
        assertThatThrownBy(() -> orderTableValidator.checkOrderStatusIn(orderTableIds))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
