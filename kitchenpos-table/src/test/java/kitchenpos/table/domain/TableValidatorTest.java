package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderFactory;
import kitchenpos.order.domain.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableValidatorTest {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    TableValidator tableValidator;

    @DisplayName("주문이 완료상태가 아니면 에러가 발생한다.")
    @Test
    void validOrdersCompletion() {
        //given
        Order order = OrderFactory.create(1, 2L, Collections.emptyList());
        given(orderRepository.findAllByOrderTableId(any())).willReturn(Collections.singletonList(order));
        //when & then
        assertThatThrownBy(() -> tableValidator.validOrdersCompletionByOrderTableId(2L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 완료상태가 아니면 단체지정을 해제할 수 없다.")
    @Test
    void validOrdersCompletionByTableGroup() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, null, 0, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 0, true);
        TableGroup tableGroup = TableGroup.from(Arrays.asList(orderTable1, orderTable2));
        Order order = OrderFactory.create(1, 2L, Collections.emptyList());
        given(orderRepository.findAllByOrderTableIdIn(any())).willReturn(Collections.singletonList(order));

        //when & then
        assertThatThrownBy(() -> tableValidator.validOrdersCompletionByTableGroup(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
