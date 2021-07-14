package kitchenpos.table.domain;

import kitchenpos.ordering.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderTableValidatorTest {

    private OrderTableValidator orderTableValidator;

    @Mock
    private OrderRepository orderRepository;

    private Long orderTable1Id = 1L;
    private Long orderTable1TableGroupId = 1L;
    private int orderTable1NumberOfGuests = 4;
    private boolean orderTable1Empty = false;

    @BeforeEach
    void setUp() {
        orderTableValidator = new OrderTableValidator(orderRepository);
    }

    @DisplayName("주문테이블 상태 검증 - 등록되어있던 주문테이블이 이미 단체지정 되어있지 않은지 확인한다.")
    @Test
    void 주문테이블이_올바르지_않으면_테이블상태를_변경할_수_없다_2() {
        OrderTable orderTableSaved = new OrderTable(orderTable1Id, 2L, orderTable1NumberOfGuests, orderTable1Empty);

        assertThatThrownBy(() -> {
            orderTableValidator.validate(orderTableSaved);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 상태 검증 - 주문테이블의 주문 상태가 계산완료인지 확인한다.")
    @Test
    void 주문테이블이_올바르지_않으면_빈테이블로_변경할_수_없다_3() {
        OrderTable orderTableSaved = new OrderTable(orderTable1Id, null, orderTable1NumberOfGuests, orderTable1Empty);

        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(true);

        assertThatThrownBy(() -> {
            orderTableValidator.validate(orderTableSaved);
        }).isInstanceOf(IllegalArgumentException.class);
    }

}
