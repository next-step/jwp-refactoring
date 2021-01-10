package kitchenpos.infra.orderTable;

import kitchenpos.application.OrderService;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.orderTable.exceptions.InvalidTryChangeEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderAdapterTest {
    private OrderAdapter orderAdapter;

    @Mock
    private OrderService orderService;

    @BeforeEach
    void setup() {
        orderAdapter = new OrderAdapter(orderService);
    }

    @DisplayName("주문 테이블과 관련된 주문이 식사중이거나 조리중인 경우 예외를 발생시킨다.")
    @Test
    void canChangeEmptyStatusTest() {
        // given
        Long orderTableId = 1L;
        given(orderService.isThisTableInMealOrCooking(orderTableId)).willReturn(true);

        // when, then
        assertThatThrownBy(() -> orderAdapter.canChangeEmptyStatus(orderTableId))
                .isInstanceOf(InvalidTryChangeEmptyException.class)
                .hasMessage("조리중이거나 식사중인 주문 테이블의 비움 상태를 바꿀 수 없습니다.");
    }
}
