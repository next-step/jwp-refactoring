package kitchenpos.tableGroup.application;

import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.tableGroup.dto.OrderTableIdRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableGroupValidatorTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TableGroupValidator tableGroupValidator;

    @DisplayName("단체 지정 할 때 주문 테이블이 2개 이상이여야 한다.")
    @Test
    void createOrderTableSizeError() {
        OrderTableIdRequest 테이블요청 = new OrderTableIdRequest(1L);

        assertThatThrownBy(
                () -> tableGroupValidator.getOrderTable(Arrays.asList(테이블요청))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("조리 또는 식사 상태이면 단제지정 해제 할 수 없다.")
    @Test
    void validateCompletion() {
        OrderTable orderTable = OrderTableFixture.생성(7, true);
        given(orderRepository.existsAllByOrderTableIdInAndOrderStatus(any(), any())).willReturn(false);

        assertThatThrownBy(
                () -> tableGroupValidator.validateCompletion(orderTable.getTableGroupId())
        ).isInstanceOf(IllegalArgumentException.class);

    }
}
