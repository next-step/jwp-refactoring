package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.NotExistIdException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTableValidatorImpl;
import kitchenpos.table.exception.NotEmptyTableException;

@ExtendWith(MockitoExtension.class)
class OrderTableValidatorImplTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderTableValidatorImpl orderTableValidator;

    @DisplayName("[주문 등록] 등록된 주문테이블만 주문 등록할 수 있다")
    @Test
    void test1() {
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderTableValidator.validate(1L))
            .isInstanceOf(NotExistIdException.class);
    }

    @DisplayName("[주문등록] 주문테이블은 비어있어야 한다")
    @Test
    void test2() {
        OrderTable orderTable = new OrderTable(false);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> orderTableValidator.validate(1L))
            .isInstanceOf(NotEmptyTableException.class);
    }

}
