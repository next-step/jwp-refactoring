package kitchenpos.table.application;

import kitchenpos.order.domain.OrderTableValidatedEvent;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderTableValidatedEventListenerTest {

    @Mock
    private OrderTableService orderTableService;

    private OrderTableValidatedEventListener orderTableValidatedEventListener;

    @BeforeEach
    void setUp() {
        orderTableValidatedEventListener = new OrderTableValidatedEventListener(orderTableService);
    }

    @DisplayName("주문이 주문 테이블이 비어있는 상태로 주어지면 예외를 던진다.")
    @Test
    void create_order_with_empty_order_table() {
        OrderTableValidatedEvent orderTableValidatedEvent = new OrderTableValidatedEvent(1L);
        OrderTable givenOrderTable = new OrderTable(1L, null, 2, true);
        when(orderTableService.findOrderTable(anyLong()))
                .thenReturn(givenOrderTable);

        assertThatThrownBy(() -> orderTableValidatedEventListener.validateOrderTable(orderTableValidatedEvent))
                .isInstanceOf(IllegalStateException.class);
    }
}
