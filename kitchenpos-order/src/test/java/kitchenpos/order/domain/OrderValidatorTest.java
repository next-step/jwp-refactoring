package kitchenpos.order.domain;

import kitchenpos.order.message.OrderMessage;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.message.OrderTableMessage;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @Mock
    private OrderRepository orderRepository;

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

    @Test
    @DisplayName("주문 테이블의 이용 여부 변경시 테이블의 상태가 조리 또는 식사중인경우 예외처리한다")
    void validateChangeEmptyTest() {
        // given
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);
        OrderTable orderTable = OrderTable.of(5, true);

        // then
        AssertionsForClassTypes.assertThatThrownBy(() -> orderValidator.validateChangeTableEmpty(orderTable))
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
        AssertionsForClassTypes.assertThatThrownBy(() -> orderValidator.validateUnGroup(orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableMessage.UN_GROUP_ERROR_INVALID_ORDER_STATE.message());

        // then
        then(orderRepository).should(times(1)).existsByOrderTableIdInAndOrderStatusIn(any(), any());
    }
}
