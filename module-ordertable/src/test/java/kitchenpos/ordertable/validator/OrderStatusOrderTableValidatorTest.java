package kitchenpos.ordertable.validator;

import java.util.Collections;
import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderStatusOrderTableValidatorTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private Order order;

    @Test
    void 조리_식사_상태의_주문이_포함_된_주문_테이블은_상태를_변경할_수_없다() {
        OrderStatusOrderTableValidator validator = new OrderStatusOrderTableValidator(orderRepository);
        BDDMockito.given(orderRepository.findByOrderTableId(ArgumentMatchers.any()))
                .willReturn(Collections.singletonList(order));
        BDDMockito.given(order.isSameStatus(ArgumentMatchers.any())).willReturn(true);

        ThrowingCallable 조리_식사_상태의_주문이_포함_된_주문_테이블일_경우 = () -> validator.validate(new OrderTable(1, false));
        Assertions.assertThatIllegalArgumentException().isThrownBy(조리_식사_상태의_주문이_포함_된_주문_테이블일_경우);
    }
}
