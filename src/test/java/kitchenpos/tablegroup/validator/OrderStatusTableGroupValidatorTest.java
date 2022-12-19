package kitchenpos.tablegroup.validator;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderStatusTableGroupValidatorTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private Order order;

    @Test
    void 조리_식사_상태의_주문이_포함_된_주문_테이블은_상태를_변경할_수_없다() {
        OrderStatusTableGroupValidator validator = new OrderStatusTableGroupValidator(orderRepository);
        given(orderRepository.findByOrderTableId(any())).willReturn(Collections.singletonList(order));
        given(order.isSameStatus(any())).willReturn(true);

        ThrowingCallable 조리_식사_상태의_주문이_포함_될_경우 = () -> validator
                .validate(Collections.singletonList(new OrderTable(1, false)));

        assertThatIllegalArgumentException().isThrownBy(조리_식사_상태의_주문이_포함_될_경우);
    }
}
