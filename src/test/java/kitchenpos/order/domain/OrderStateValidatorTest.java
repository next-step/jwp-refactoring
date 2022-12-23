package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 상태 validation 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderStateValidatorTest {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderStateValidator orderStateValidator;

    private Long 주문_테이블_1_id;
    private OrderTable 주문_테이블;

    @BeforeEach
    public void setUp() {
        주문_테이블_1_id = 1L;
        주문_테이블 = new OrderTable(주문_테이블_1_id, null, new NumberOfGuests(0), true);
    }

    @DisplayName("계산 완료되지 않은 주문이 등록되어 있는 경우 빈 테이블 여부 수정 요청 시 예외처리")
    @Test
    void 계산_완료_안된_주문_등록된_빈_테이블_여부_수정_예외처리() {
        List<Order> orders = Collections.singletonList(new Order(주문_테이블_1_id, OrderStatus.MEAL));
        when(orderRepository.findByOrderTableId(주문_테이블_1_id)).thenReturn(orders);

        assertThatThrownBy(() -> orderStateValidator.checkNotCompletedOrderExist(주문_테이블)).isInstanceOf(
                IllegalArgumentException.class);
    }
}
