package kitchenpos.order.application;

import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableValidatorTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TableValidator tableValidator;

    @DisplayName("조리 또는 식사 상태이면 빈 테이블로 만들 수 없다. ")
    @Test
    void validateCompletion() {
        OrderTable orderTable = OrderTableFixture.생성(7, true);
        given(orderRepository.existsAllByOrderTableIdInAndOrderStatus(any(), any())).willReturn(false);

        assertThatThrownBy(
                () -> tableValidator.validateCompletion(orderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
