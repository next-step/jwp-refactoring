package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kitchenpos.exception.InvalidArgumentException;
import kitchenpos.exception.NotFoundException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 정합성 체크 테스트")
@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderValidator orderValidator;


    @Test
    @DisplayName("주문 생성 validate 체크: 테이블 정보는 필수, 빈 테이블인 경우 주문을 생성할 수 없다.")
    void validateCreateOrder() {
        assertThatThrownBy(() -> orderValidator.validateCreateOrder(null))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("해당하는 테이블이 없습니다.");

        when(orderTableRepository.findById(anyLong()))
            .thenReturn(Optional.of(OrderTable.of(0, true)));

        assertThatThrownBy(() -> orderValidator.validateCreateOrder(1L))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("빈 테이블은 주문을 할 수 없습니다.");
    }
}