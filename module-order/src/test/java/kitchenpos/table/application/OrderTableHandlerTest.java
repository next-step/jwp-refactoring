package kitchenpos.table.application;

import kitchenpos.order.application.OrderTableValidateEvent;
import kitchenpos.table.domain.Empty;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderTableHandlerTest {

    @InjectMocks
    private OrderTableHandler orderTableHandler;

    @Mock
    private TableService tableService;

    private Long orderTableId;

    @BeforeEach
    public void setUp() {
        orderTableId = 1L;
    }

    @DisplayName("주문 생성 유효성 검증 성공 테스트")
    @Test
    void validate_success() {
        // given
        OrderTable orderTable = OrderTable.of(orderTableId, Empty.of(false));

        given(tableService.existsById(orderTableId)).willReturn(Boolean.TRUE);
        given(tableService.findById(orderTableId)).willReturn(orderTable);

        // when & then
        assertThatNoException()
                .isThrownBy(() -> orderTableHandler.validateOrder(new OrderTableValidateEvent(orderTableId)));
    }

    @DisplayName("주문 생성 유효성 검증 실패 테스트 - 주문 테이블 존재하지 않음")
    @Test
    void validateCreate_failure_validateOrderTable() {
        // given
        given(tableService.existsById(orderTableId)).willReturn(Boolean.FALSE);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTableHandler.validateOrder(new OrderTableValidateEvent(orderTableId)));
    }

    @DisplayName("주문 생성 유효성 검증 실패 테스트 - 주문 테이블 비어있음")
    @Test
    void validate_failure_validateOrderTableIsEmpty() {
        // given
        OrderTable orderTable = OrderTable.of(orderTableId, Empty.of(true));

        given(tableService.existsById(orderTableId)).willReturn(Boolean.TRUE);
        given(tableService.findById(orderTableId)).willReturn(orderTable);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTableHandler.validateOrder(new OrderTableValidateEvent(orderTableId)));
    }
}
