package kitchenpos.table.application;


import static kitchenpos.fixture.OrderTableFactory.createOrderTable;
import static kitchenpos.order.domain.OrderStatus.getCannotUngroupTableGroupStatus;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import kitchenpos.Exception.UnCompletedOrderStatusException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupValidatorTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TableGroupValidator tableGroupValidator;

    @Test
    void 주문상태_조리_혹은_식사_예외() {
        // given
        TableGroup 단체지정 = new TableGroup(1L, null,
                Arrays.asList(createOrderTable(1L, null, 5, true), createOrderTable(2L, null, 5, true)));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L, 2L),
                getCannotUngroupTableGroupStatus())).willReturn(true);

        // when, then
        assertThatThrownBy(
                () -> tableGroupValidator.validate(단체지정)
        ).isInstanceOf(UnCompletedOrderStatusException.class);
    }
}
