package kitchenpos.table.application;

import static kitchenpos.table.application.OrderTableValidatorTest.주문_항목_목록;
import static kitchenpos.table.application.OrderTableValidatorTest.주문테이블;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.exception.CannotUngroupOrderTableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("단체지정 유효성검증 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupValidatorTest {

    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private TableGroupValidator tableGroupValidator;

    @DisplayName("단체지정 해제시 진행중(조리 or 식사)인 단계의 주문 이력이 존재할 경우 해제가 불가능하다.")
    @Test
    void validationUpgroup() {
        // Given
        List<Long> orderTableIds = new ArrayList<>(Arrays.asList(1L, 2L));
        Order 주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING, LocalDateTime.now(), 주문_항목_목록);
        given(orderRepository.findAllByOrderTableId(any())).willReturn(new ArrayList<>(Arrays.asList(주문)));

        // When
        assertThatThrownBy(() -> tableGroupValidator.validationUpgroup(orderTableIds))
            .isInstanceOf(CannotUngroupOrderTableException.class);
    }

}
