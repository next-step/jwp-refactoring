package kitchenpos.ordertable.application;

import kitchenpos.order.domain.OrderValidator;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.exception.BadRequestException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.utils.Message.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@DisplayName("주문 테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderValidator orderValidator;

    @InjectMocks
    private TableService tableService;

    private OrderTable notEmptyTable;
    private OrderTable emptyTable;

    @BeforeEach
    void setUp() {
        notEmptyTable = OrderTable.of(null, null, 2, false);
        emptyTable = OrderTable.of(null, null,0, true);
    }

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void create() {
        when(orderTableRepository.save(any())).thenReturn(notEmptyTable);

        OrderTableResponse result = tableService.create(notEmptyTable);

        assertThat(result).isEqualTo(OrderTableResponse.from(notEmptyTable));
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() {
        when(orderTableRepository.findAll()).thenReturn(Arrays.asList(notEmptyTable, emptyTable));

        List<OrderTableResponse> results = tableService.list();

        assertAll(
                () -> assertThat(results).hasSize(2),
                () -> assertThat(results).containsExactly(
                        OrderTableResponse.from(notEmptyTable),
                        OrderTableResponse.from(emptyTable)
                )
        );
    }

    @DisplayName("등록되지 않은 주문 테이블의 빈 상태를 변경할 수 없다.")
    @Test
    void changeEmptyException() {
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        boolean empty = notEmptyTable.isEmpty();
        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(1L, empty))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정된 주문 테이블의 빈 상태를 변경할 수 없다.")
    @Test
    void changeEmptyException2() {
        notEmptyTable.setTableGroupId(1L);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(notEmptyTable));

        boolean empty = notEmptyTable.isEmpty();
        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(1L, empty))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(INVALID_CHANGE_TO_EMPTY_GROUPED_TABLE);
    }

    @DisplayName("주문 상태가 조리이면 주문 테이블의 빈 상태를 변경할 수 없다.")
    @Test
    void changeEmptyException3() {
        doThrow(new BadRequestException(INVALID_CANCEL_ORDER_TABLES_STATUS))
                .when(orderValidator).checkEmptyChangeable(any());

        boolean empty = notEmptyTable.isEmpty();
        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(1L, empty))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(INVALID_CANCEL_ORDER_TABLES_STATUS);
    }

    @DisplayName("주문 테이블의 빈 상태를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        boolean isEmpty = notEmptyTable.isEmpty();
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(notEmptyTable));

        OrderTableResponse result = tableService.changeEmpty(1L, !notEmptyTable.isEmpty());

        Assertions.assertThat(result.isEmpty()).isEqualTo(!isEmpty);
    }

    @DisplayName("방문한 손님 수가 0보다 작은경우 주문 테이블의 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsException() {
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(notEmptyTable));

        Assertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, -1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(INVALID_UNDER_ZERO_GUESTS);
    }

    @DisplayName("등록되지 않은 주문 테이블의 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsException2() {
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        Long orderTableId = notEmptyTable.getId();
        OrderTable orderTable = OrderTable.of(orderTableId, null, 4, notEmptyTable.isEmpty());
        int numberOfGuests = orderTable.getNumberOfGuests();
        Assertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, numberOfGuests))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 주문 테이블이 빈 상태이면 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsException3() {
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(emptyTable));

        Long orderTableId = emptyTable.getId();
        OrderTable orderTable = OrderTable.of(orderTableId, 2, emptyTable.isEmpty());
        int numberOfGuests = orderTable.getNumberOfGuests();
        Assertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, numberOfGuests))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(CAN_NOT_CHANGE_NUMBER_OF_GUESTS);
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = OrderTable.of(notEmptyTable.getId(), 4, notEmptyTable.isEmpty());

        when(orderTableRepository.findById(any())).thenReturn(Optional.of(notEmptyTable));

        int numberOfGuests = orderTable.getNumberOfGuests();
        OrderTableResponse result = tableService.changeNumberOfGuests(orderTable.getId(), numberOfGuests);

        Assertions.assertThat(result.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }
}
