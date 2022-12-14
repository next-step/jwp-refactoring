package kitchenpos.ordertable.validator;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.fixture.TestOrderFactory;
import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@DisplayName("주문테이블 Validator 테스트")
@ExtendWith(MockitoExtension.class)
class OrderTableValidatorTest {
    @InjectMocks
    private OrderTableValidator orderTableValidator;

    @Mock
    private OrderRepository orderRepository;

    @DisplayName("주문테이블의 빈 상태 변경시 정상적으로 유효성 검사가 성공한다")
    @Test
    void validateUpdateEmpty() {
        // given
        Order order = TestOrderFactory.createCompleteOrder();
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);

        when(orderRepository.findAllByOrderTableId(orderTable.getId())).thenReturn(Arrays.asList(order));

        // when & then
        assertDoesNotThrow(() -> orderTableValidator.validateUpdateEmpty(orderTable));
    }

    @DisplayName("주문테이블의 빈 상태 변경시 주문테이블이 단체지정되어 있다면 예외가 발생한다.")
    @Test
    void validateUpdateGroupTableEmpty() {
        // given
        Order order = TestOrderFactory.createCompleteOrder();
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);
        orderTable.setTableGroupId(1L);

        // when & then
        assertThatThrownBy(() -> orderTableValidator.validateUpdateEmpty(orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ALREADY_TABLE_GROUP.getMessage());
    }

    @DisplayName("주문테이블의 빈 상태 변경시 주문이 결제완료 상태가 아니라면 예외가 발생한다.")
    @Test
    void validateUpdateEmptyNotCompleteOrder() {
        // given
        Order order = TestOrderFactory.createMealOrder();
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);

        when(orderRepository.findAllByOrderTableId(orderTable.getId())).thenReturn(Arrays.asList(order));

        // when & then
        assertThatThrownBy(() -> orderTableValidator.validateUpdateEmpty(orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_STATUS_NOT_COMPLETE.getMessage());
    }

    @DisplayName("주문테이블의 손님 수 변경시 정상적으로 유효성 검사가 성공한다")
    @Test
    void validateUpdateNumberOfGuests() {
        // given
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);

        // when & then
        assertDoesNotThrow(() -> orderTableValidator.validateUpdateNumberOfGuests(orderTable));
    }

    @DisplayName("주문테이블의 손님수 변경시 빈상태의 테이블이라면 예외가 발생한다.")
    @Test
    void validateUpdateNotEmptyNumberOfGuests() {
        // given
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), true);

        // when & then
        assertThatThrownBy(() -> orderTableValidator.validateUpdateNumberOfGuests(orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_TABLE_IS_EMPTY_STATUS.getMessage());
    }
}
