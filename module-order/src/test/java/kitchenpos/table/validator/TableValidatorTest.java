package kitchenpos.table.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.TableValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableValidatorTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private TableValidator tableValidator;

    @DisplayName("조리중이거나 식사중인 주문의 상태를 변경 시, IllegalArgumentException 이 발생한다.")
    @Test
    void invalid_orderStatus() {
        //given
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(true);

        //when & then
        assertThatThrownBy(() -> tableValidator.validateOrderStatus(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("현재 조리중이거나 식사중인 주문이 존재합니다.");
    }

}
