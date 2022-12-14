package kitchenpos.ordertable.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.ExceptionMessage;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.exception.CannotChangeEmptyException;
import kitchenpos.ordertable.exception.CannotChangeNumberOfGuestsException;
import kitchenpos.ordertable.exception.InvalidNumberOfGuestsSize;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    private OrderTable 비어있지않은_주문_테이블;
    private OrderTable 비어있는_주문_테이블;

    @BeforeEach
    void setUp() {
        비어있지않은_주문_테이블 = OrderTable.of(null, 2, false);
        비어있는_주문_테이블 = OrderTable.of(null, 0, true);
    }

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void create() {
        when(orderTableRepository.save(any())).thenReturn(비어있지않은_주문_테이블);

        OrderTableResponse result = tableService.create(비어있지않은_주문_테이블);

        assertThat(result).isEqualTo(OrderTableResponse.from(비어있지않은_주문_테이블));
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() {
        when(orderTableRepository.findAll()).thenReturn(Arrays.asList(비어있지않은_주문_테이블, 비어있는_주문_테이블));

        List<OrderTableResponse> results = tableService.list();

        assertAll(
                () -> assertThat(results).hasSize(2),
                () -> assertThat(results).containsExactly(
                        OrderTableResponse.from(비어있지않은_주문_테이블),
                        OrderTableResponse.from(비어있는_주문_테이블)
                )
        );
    }

    @DisplayName("등록되지 않은 주문 테이블의 빈 상태를 변경할 수 없다.")
    @Test
    void changeEmptyException() {
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        boolean empty = 비어있지않은_주문_테이블.isEmpty();
        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(1L, empty))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ExceptionMessage.ORDER_TABLE_NOT_FOUND);
    }

    @DisplayName("단체 지정된 주문 테이블의 빈 상태를 변경할 수 없다.")
    @Test
    void changeEmptyException2() {
        비어있지않은_주문_테이블.setTableGroupId(1L);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(비어있지않은_주문_테이블));

        boolean empty = 비어있지않은_주문_테이블.isEmpty();
        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(1L, empty))
                .isInstanceOf(CannotChangeEmptyException.class)
                .hasMessageStartingWith(ExceptionMessage.CAN_NOT_CHANGE_EMPTY_WHEN_TABLE_GROUPED);
    }

    @DisplayName("주문 상태가 조리이면 주문 테이블의 빈 상태를 변경할 수 없다.")
    @Test
    void changeEmptyException3() {
        Order.of(비어있지않은_주문_테이블.getId(), Arrays.asList(OrderLineItem.of(1L, 2)));
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(비어있지않은_주문_테이블));

        boolean empty = 비어있지않은_주문_테이블.isEmpty();
        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(1L, empty))
                .isInstanceOf(CannotChangeEmptyException.class)
                .hasMessageStartingWith(ExceptionMessage.CAN_NOT_CHANGE_EMPTY_WHEN_COOKING_OR_MEAL);
    }

    @DisplayName("주문 상태가 식사이면 주문 테이블의 빈 상태를 변경할 수 없다.")
    @Test
    void changeEmptyException4() {
        Order 주문 = Order.of(비어있지않은_주문_테이블.getId(), Arrays.asList(OrderLineItem.of(1L, 2)));
        주문.changeOrderStatus(OrderStatus.MEAL);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(비어있지않은_주문_테이블));

        boolean empty = 비어있지않은_주문_테이블.isEmpty();
        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(1L, empty))
                .isInstanceOf(CannotChangeEmptyException.class)
                .hasMessageStartingWith(ExceptionMessage.CAN_NOT_CHANGE_EMPTY_WHEN_COOKING_OR_MEAL);
    }

    @DisplayName("주문 테이블의 빈 상태를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        boolean isEmpty = 비어있지않은_주문_테이블.isEmpty();
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(비어있지않은_주문_테이블));

        OrderTableResponse result = tableService.changeEmpty(1L, !비어있지않은_주문_테이블.isEmpty());

        Assertions.assertThat(result.isEmpty()).isEqualTo(!isEmpty);
    }

    @DisplayName("방문한 손님 수가 0보다 작은경우 주문 테이블의 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsException() {
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(비어있지않은_주문_테이블));

        Assertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, -1))
                .isInstanceOf(InvalidNumberOfGuestsSize.class)
                .hasMessageStartingWith(ExceptionMessage.INVALID_NUMBER_OF_GUESTS_SIZE);
    }

    @DisplayName("등록되지 않은 주문 테이블의 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsException2() {
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        Long orderTableId = 비어있지않은_주문_테이블.getId();
        OrderTable orderTable = OrderTable.of(orderTableId, 4, 비어있지않은_주문_테이블.isEmpty());
        int numberOfGuests = orderTable.getNumberOfGuests();
        Assertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, numberOfGuests))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ExceptionMessage.ORDER_TABLE_NOT_FOUND);
    }

    @DisplayName("등록된 주문 테이블이 빈 상태이면 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsException3() {
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(비어있는_주문_테이블));

        Long orderTableId = 비어있는_주문_테이블.getId();
        OrderTable orderTable = OrderTable.of(orderTableId, 2, 비어있는_주문_테이블.isEmpty());
        int numberOfGuests = orderTable.getNumberOfGuests();
        Assertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, numberOfGuests))
                .isInstanceOf(CannotChangeNumberOfGuestsException.class)
                .hasMessageStartingWith(ExceptionMessage.CAN_NOT_CHANGE_NUMBER_OF_GUESTS);
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = OrderTable.of(비어있지않은_주문_테이블.getId(), 4, 비어있지않은_주문_테이블.isEmpty());

        when(orderTableRepository.findById(any())).thenReturn(Optional.of(비어있지않은_주문_테이블));

        int numberOfGuests = orderTable.getNumberOfGuests();
        OrderTableResponse result = tableService.changeNumberOfGuests(orderTable.getId(), numberOfGuests);

        Assertions.assertThat(result.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }
}
