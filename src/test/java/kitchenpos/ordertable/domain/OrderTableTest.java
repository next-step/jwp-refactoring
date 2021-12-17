package kitchenpos.ordertable.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@DisplayName("테이블 도메인 테스트")
class OrderTableTest {

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable(false);
    }

    @Test
    @DisplayName("테이블의 상태를 변경한다.")
    void changeEmpty() {
        // given
        OrderTableRepository orderTableRepository = mock(OrderTableRepository.class);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));

        Order order = new Order(OrderStatus.COMPLETION);
        order.changeOrderTable(1L, new OrderValidator(orderTableRepository));

        // when
        orderTable.changeEmpty(true, new OrderTableValidator(mock(OrderRepository.class)));

        // then
        assertThat(orderTable.getEmpty()).isEqualTo(new Empty(true));
    }

    @Test
    @DisplayName("방문한 손님 수를 변경한다.")
    void changeNumberOfGuests() {
        // when
        orderTable.changeNumberOfGuests(1, new OrderTableValidator(mock(OrderRepository.class)));

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(new NumberOfGuests(1));
    }

    @Test
    @DisplayName("0명 이하의 손님 수로 테이블의 방문한 손님 수를 변경하면 예외를 발생한다.")
    void changeNumberOfGuestsThrowException1() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderTable.changeNumberOfGuests(-1, new OrderTableValidator(mock(OrderRepository.class))))
                .withMessageMatching(NumberOfGuests.MESSAGE_VALIDATE);
    }

    @Test
    @DisplayName("비어있는 테이블의 방문한 손님 수를 변경하면 예외를 발생한다.")
    void changeNumberOfGuestsThrowException2() {
        // given
        orderTable.changeEmpty(true, new OrderTableValidator(mock(OrderRepository.class)));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderTable.changeNumberOfGuests(1, new OrderTableValidator(mock(OrderRepository.class))))
                .withMessageMatching(Empty.MESSAGE_VALIDATE_CHANGABLE);
    }
}
