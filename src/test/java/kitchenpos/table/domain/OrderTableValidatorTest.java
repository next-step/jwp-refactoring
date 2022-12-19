package kitchenpos.table.domain;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.message.OrderTableMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OrderTableValidatorTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderTableValidator orderTableValidator;

    @Test
    @DisplayName("주문 테이블의 이용 여부 변경시 테이블의 상태가 조리 또는 식사중인경우 예외처리한다")
    void validateChangeEmptyTest() {
        // given
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);
        OrderTable orderTable = OrderTable.of(5, true);

        // then
        assertThatThrownBy(() -> orderTableValidator.validateChangeEmpty(orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableMessage.CHANGE_EMPTY_ERROR_INVALID_ORDER_STATE.message());

        // then
        then(orderRepository).should(times(1)).existsByOrderTableIdInAndOrderStatusIn(any(), any());
    }

    @Test
    @DisplayName("테이블 그룹 해지시 속해져있는 테이블중 조리 또는 식사 상태인경우 예외처리되어 해지에 실패한다")
    void validateUnGroupTest() {
        // given
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);
        OrderTables orderTables = new OrderTables(Arrays.asList(
                OrderTable.of(1, true),
                OrderTable.of(2, true)
        ));

        // then
        assertThatThrownBy(() -> orderTableValidator.validateUnGroup(orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableMessage.UN_GROUP_ERROR_INVALID_ORDER_STATE.message());

        // then
        then(orderRepository).should(times(1)).existsByOrderTableIdInAndOrderStatusIn(any(), any());
    }
}
