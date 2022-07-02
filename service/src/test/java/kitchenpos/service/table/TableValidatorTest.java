package kitchenpos.service.table;

import kitchenpos.domain.order.OrdersRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.service.table.application.TableValidator;
import kitchenpos.service.table.dto.OrderTableUpdateNumberOfGuestsRequest;
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
class TableValidatorTest {
    @Mock
    private OrdersRepository ordersRepository;
    @InjectMocks
    private TableValidator tableValidator;

    @Test
    @DisplayName("주문 테이블의 단체 지정이 되어있으면 빈 테이블로 변경 실패한다.")
    void validate_changeEmpty_failed_1() {
        //given
        OrderTable orderTable = OrderTable.of(2L, 5, false);
        //then
        assertThatThrownBy(() -> tableValidator.validateChangeEmpty(orderTable)).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 상태가 조리, 식사 인 경우 빈 테이블로 변경 실패한다.")
    void validate_changeEmpty_failed_2() {
        //given
        given(ordersRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(true);

        //then
        assertThatThrownBy(
                () -> tableValidator.validateChangeEmpty(OrderTable.of(10, true))).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 방문한 손님 수가 음수면 방문한 손님 수 변경에 실패한다.")
    void changeNumberOfGuests_failed_1() {
        //then
        assertThatThrownBy(() -> tableValidator.validateChangeNumberOfGuests(OrderTable.of(10, false),
                new OrderTableUpdateNumberOfGuestsRequest(-1))).isExactlyInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("주문 테이블이 비어있으면 방문한 손님 수 변경에 실패한다.")
    void changeNumberOfGuests_failed_2() {
        //then
        assertThatThrownBy(() -> tableValidator.validateChangeNumberOfGuests(OrderTable.of( 10, true),
                new OrderTableUpdateNumberOfGuestsRequest(-1))).isExactlyInstanceOf(IllegalArgumentException.class);

    }

}
