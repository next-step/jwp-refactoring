package kitchenpos.order.validator;

import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.exception.TableException;
import kitchenpos.table.validator.OrderValidatorImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderValidatorTest {
    @Mock
    OrderTableRepository orderTableRepository;
    @InjectMocks
    OrderValidatorImpl orderValidator;

    @Test
    @DisplayName("테이블 변경 가능")
    void success() {
        // given
        given(orderTableRepository.findById(any()))
                .willReturn(Optional.of(OrderTable.of(0, true)));
        // when
        orderValidator.validate(new OrderRequest(1L, Arrays.asList(new OrderLineItemRequest(1L, 5))));
    }

    @Test
    @DisplayName("테이블 사용 중 변경 오류")
    void exception() {
        // given
        given(orderTableRepository.findById(any()))
                .willReturn(Optional.of(OrderTable.of(0, false)));
        // when
        assertThatThrownBy(() -> orderValidator.validate(new OrderRequest(1L,
                Arrays.asList(new OrderLineItemRequest(1L, 5)))))
                .isInstanceOf(TableException.class);
    }
}
