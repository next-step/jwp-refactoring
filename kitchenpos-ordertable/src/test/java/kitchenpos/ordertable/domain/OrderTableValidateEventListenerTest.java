package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.common.event.OrderCreateEvent;
import kitchenpos.ordertable.exception.ClosedTableOrderException;

import kitchenpos.ordertable.TableTestFixtures;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import kitchenpos.ordertable.vo.NumberOfGuests;

@ExtendWith(MockitoExtension.class)
class OrderTableValidateEventListenerTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderTableValidateEventListener orderTableValidateEventListener;

    @DisplayName("테이블이 주문종료 상태이면 예외")
    @Test
    void validateNotOrderClosedTable() {
        //given
        OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(6), true);
        TableTestFixtures.특정_주문테이블_조회_모킹(orderTableRepository, orderTable);
        OrderCreateEvent orderCreateEvent = new OrderCreateEvent(this, orderTable.getId());
        //when, then
        assertThatThrownBy(
                () -> orderTableValidateEventListener.validateNotOrderClosedTable(orderCreateEvent))
            .isInstanceOf(ClosedTableOrderException.class);
    }
}
