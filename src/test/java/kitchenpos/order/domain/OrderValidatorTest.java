package kitchenpos.order.domain;

import kitchenpos.order.message.OrderMessage;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderValidator orderValidator;

    @Test
    @DisplayName("주문 테이블이 빈테이블인 경우 예외처리한다")
    void validateOrderTableTest() {
        // given
        OrderTable orderTable = OrderTable.of(5, true);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));

        // when
        assertThatThrownBy(() -> orderValidator.validateOrderTable(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderMessage.CREATE_ERROR_ORDER_TABLE_IS_EMPTY.message());

        // then
        then(orderTableRepository).should(times(1)).findById(1L);
    }
}
