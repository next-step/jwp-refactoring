package kitchenpos.ordertable.validator;

import java.util.Optional;
import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTableExistAndEmptyValidatorTest {

    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private Order order;

    @Test
    void 주문_등록시_등록된_주문_테이블만_지정할_수_있다() {
        OrderTableExistAndEmptyValidator validator = new OrderTableExistAndEmptyValidator(orderTableRepository);
        BDDMockito.given(orderTableRepository.findById(ArgumentMatchers.any())).willReturn(Optional.empty());

        ThrowingCallable 주문_등록시_등록되지_않은_주문_테이블을_지정한_경우 = () -> validator.validate(order.getOrderTableId());
        Assertions.assertThatIllegalArgumentException().isThrownBy(주문_등록시_등록되지_않은_주문_테이블을_지정한_경우);
    }
}
