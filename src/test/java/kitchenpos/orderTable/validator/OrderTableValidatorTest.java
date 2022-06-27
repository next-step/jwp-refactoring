package kitchenpos.orderTable.validator;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static kitchenpos.utils.DomainFixtureFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.orderTable.domain.OrderTable;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTableValidatorTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderTableValidator orderTableValidator;
    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        주문테이블 = createOrderTable(1L, 2, false);
    }

    @DisplayName("주문테이블의 주문이 완료상태가 아닌경우 테스트")
    @Test
    void validateComplete() {
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(주문테이블.id(), Arrays.asList(COOKING, MEAL))).willReturn(true);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTableValidator.validateComplete(주문테이블.id()))
                .withMessage("주문테이블의 주문이 완료상태가 아닙니다.");
    }
}
