package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 테이블 validation service 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderTableValidatorTest {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderTableValidator orderTableValidator;

    private Long 주문_테이블_1_id;
    private OrderTable 주문_테이블;

    @BeforeEach
    void setUp() {
        주문_테이블_1_id = 1L;
        주문_테이블 = new OrderTable(주문_테이블_1_id, null, new NumberOfGuests(0), true);
    }

    @DisplayName("단체 지정되어 있는 경우 빈 테이블 여부 수정 요청 시 예외처리")
    @Test
    void 단체_지정_주문_테이블_빈_테이블_여부_수정_예외처리() {
        TableGroup 단체 = new TableGroup(1L, null);
        Long 단체_지정된_주문_테이블_id = 2L;
        OrderTable 단체_지정된_주문_테이블 = new OrderTable(단체_지정된_주문_테이블_id, 단체.getId(), new NumberOfGuests(0), true);

        assertThatThrownBy(() -> orderTableValidator.validateEmptyChangable(단체_지정된_주문_테이블)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("계산 완료되지 않은 주문이 등록되어 있는 경우 빈 테이블 여부 수정 요청 시 예외처리")
    @Test
    void 계산_완료_안된_주문_등록된_빈_테이블_여부_수정_예외처리() {
        List<Order> orders = Collections.singletonList(new Order(주문_테이블_1_id, OrderStatus.MEAL));
        when(orderRepository.findByOrderTableId(주문_테이블_1_id)).thenReturn(orders);

        assertThatThrownBy(() -> orderTableValidator.validateEmptyChangable(주문_테이블)).isInstanceOf(
                IllegalArgumentException.class);
    }
}
