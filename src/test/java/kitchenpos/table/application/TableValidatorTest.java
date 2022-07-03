package kitchenpos.table.application;


import static kitchenpos.fixture.OrderTableFactory.createOrderTable;
import static kitchenpos.order.domain.OrderStatus.getCannotUngroupTableGroupStatus;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import kitchenpos.Exception.UnCompletedOrderStatusException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableValidatorTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private TableValidator tableValidator;

    @Test
    void 주문상태_조리_혹은_식사_예외() {
        // given
        OrderTable orderTable = createOrderTable(1L, null, 5, false);
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
                getCannotUngroupTableGroupStatus())).willThrow(
                UnCompletedOrderStatusException.class);

        // when, then
        assertThatThrownBy(
                () -> tableValidator.validate(orderTable.getId())
        ).isInstanceOf(UnCompletedOrderStatusException.class);
    }
}
