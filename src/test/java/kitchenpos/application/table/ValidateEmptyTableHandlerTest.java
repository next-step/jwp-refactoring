package kitchenpos.application.table;

import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.event.orders.ValidateEmptyTableEvent;

@ExtendWith(MockitoExtension.class)
public class ValidateEmptyTableHandlerTest {
    @Mock
    OrderTableRepository orderTableRepository;

    @InjectMocks
    ValidateEmptyTableHandler validateEmptyTableHandler;

    @DisplayName("확인 요청된 주문테이블이 빈테이블이면 에러가 발생된다.")
    @Test
    void event_validateEmptyTableHandler() {
        // given
        OrderTable 주문테이블 = OrderTable.of(0, true);
        
        ValidateEmptyTableEvent validateEmptyTableEvent = new ValidateEmptyTableEvent(1L);

        when(orderTableRepository.findById(nullable(Long.class))).thenReturn(Optional.of(주문테이블));

        // when 
        // then   
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> validateEmptyTableHandler.handle(validateEmptyTableEvent));
    }
}
