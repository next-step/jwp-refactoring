package kitchenpos.order.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    private OrderValidator orderValidator;

    @BeforeEach
    void setUp() {
        orderValidator = new OrderValidator(orderTableRepository);
    }

    @DisplayName("주문이 주문 테이블이 비어있는 상태로 주어지면 예외를 던진다.")
    @Test
    void create_order_with_empty_order_table() {
        OrderTable givenOrderTable = new OrderTable(1L, null, 2, true);
        when(orderTableRepository.findById(anyLong()))
                .thenReturn(Optional.of(givenOrderTable));

        assertThatThrownBy(() -> orderValidator.validateOrderTable(1L))
                .isInstanceOf(IllegalStateException.class);
    }

}
