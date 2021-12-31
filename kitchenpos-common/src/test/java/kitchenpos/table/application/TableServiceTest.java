package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import kitchenpos.order.application.OrderService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderService orderService;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 완료 상태가 아닌 테이블을 비우려고 하면 예외가 발생한다.")
    @Test
    void changeEmptyOrderStatusNoeCompleted() {
        OrderTable 주문완료아닌_테이블 = new OrderTable(2, true);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문완료아닌_테이블));
        given(orderService.isOrderStatusCompleted(anyLong())).willReturn(false);

        assertThatThrownBy(() -> tableService.changeEmpty(1L))
            .isInstanceOf(IllegalArgumentException.class);
    }
}