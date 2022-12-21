package kitchenpos.unit.validator;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.type.OrderStatus;
import kitchenpos.order.port.OrderPort;
import kitchenpos.order.validator.OrderTableValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderTableValidatorTest {

    @Mock
    private OrderPort orderPort;

    @InjectMocks
    private OrderTableValidator orderTableValidator;

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @DisplayName("주문상태는 조리, 식사중이면 변경이 불가능하다")
    void validCheckIsNotNullTableGroup(OrderStatus status) {
        Order order = new Order(1L, 1L, status, null);

        given(orderPort.findByOrderTableId(any())).willReturn(Arrays.asList(order));


        assertThatThrownBy(() ->
                orderTableValidator.validChangeEmpty(new OrderTable(null, 1, true))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("손님수 변경은 0명이하이면 안된다.")
    void validChangeNumberOfGuest() {
        assertThatThrownBy(() ->
                orderTableValidator.validChangeNumberOfGuest(-1)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
