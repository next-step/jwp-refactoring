package kitchenpos.infra.tableGroup;

import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.tableGroup.exceptions.InvalidTableUngroupTryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderAdapterInTableGroupTest {
    private OrderAdapterInTableGroup orderAdapterInTableGroup;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setup() {
        orderAdapterInTableGroup = new OrderAdapterInTableGroup(orderRepository);
    }

    @DisplayName("조리 중이거나 식사 중이 주문의 단체석 해제 여부를 물어볼 경우 예외 발생")
    @Test
    void canUngroupTest() {
        // given
        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))).willReturn(true);

        // when, then
        assertThatThrownBy(() -> orderAdapterInTableGroup.canUngroup(orderTableIds))
                .isInstanceOf(InvalidTableUngroupTryException.class)
                .hasMessage("조리 중이거나 식사 중인 단체 지정을 해제할 수 없습니다.");
    }
}
