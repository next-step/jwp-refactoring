package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.exception.NotCompletionStatusException;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.GuestNumber;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    private TableService tableService;
    private OrderTable orderTable;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableValidator tableValidator;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderTableRepository, tableValidator);

        orderTable = new OrderTable.Builder()
                .setId(1L)
                .setGuestNumber(GuestNumber.of(5))
                .setEmpty(false)
                .build();
    }

    @Test
    @DisplayName("주문 테이블을 생성한다.")
    void createOrderTable() {
        // given
        final OrderTableResponse expectedOrderTableResponse = orderTable.toOrderTableResponse();
        when(orderTableRepository.save((any()))).thenReturn(orderTable);
        // when
        final OrderTableResponse actual = tableService.create(new OrderTableRequest(5, false));
        // then
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(expectedOrderTableResponse.getId()),
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(expectedOrderTableResponse.getNumberOfGuests()),
                () -> assertThat(actual.isEmpty()).isEqualTo(expectedOrderTableResponse.isEmpty())
        );
    }

    @Test
    @DisplayName("주문 테이블들을 조회한다.")
    void searchOrderTable() {
        // given
        final OrderTableResponse expectedOrderTableResponse = orderTable.toOrderTableResponse();
        when(orderTableRepository.findAll()).thenReturn(Arrays.asList(orderTable));
        // when
        final List<OrderTableResponse> actual = tableService.list();
        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).getId()).isEqualTo(expectedOrderTableResponse.getId()),
                () -> assertThat(actual.get(0).getNumberOfGuests()).isEqualTo(
                        expectedOrderTableResponse.getNumberOfGuests()),
                () -> assertThat(actual.get(0).isEmpty()).isEqualTo(expectedOrderTableResponse.isEmpty())
        );
    }

    @Test
    @DisplayName("주문 테이블을 빈 테이블로 변경한다.")
    void changeEmptyOrderTable() {
        // given
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));
        doNothing().when(tableValidator).validateNotCompletionOrderTable(any());
        // when
        final OrderTableResponse actual = tableService.changeEmpty(1L);
        // then
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("테이블 주문번호가 존재하지 않으면 예외 발생")
    void notExistOrderTableId() {
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(1L));
    }

    @Test
    @DisplayName("빈 테이블로 변경할 테이블이 단체 그룹이면 예외 발생")
    void notTableGroup() {
        // given
        final OrderTable fullOrderTableGroup = new OrderTable.Builder()
                .setId(1L)
                .setTableGroupId(1L)
                .setGuestNumber(GuestNumber.of(5))
                .setEmpty(false)
                .build();

        when(orderTableRepository.findById(any())).thenReturn(Optional.of(fullOrderTableGroup));
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(1L));
    }

    @Test
    @DisplayName("주문 테이블이 요리중이거나 식사중이면 예외 발생")
    void cookingAndMealOrderTable() {
        // given
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));
        doThrow(new NotCompletionStatusException()).when(tableValidator).validateNotCompletionOrderTable(any());
        // when && then
        assertThatIllegalStateException()
                .isThrownBy(() -> tableService.changeEmpty(1L));
    }

    @Test
    @DisplayName("주문 테이블의 방문 고객수를 변경한다.")
    void changeNumberOfGuests() {
        // given
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));
        // when
        final OrderTableResponse actual = tableService.changeNumberOfGuests(1L, 3);
        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(3);
    }

    @Test
    @DisplayName("변경할 고객이 0명 미만이면 예외 발생")
    void negativeNumberOfGuests() {
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(1L, -1));
    }

    @Test
    @DisplayName("방문 고객 변경할 테이블 주문번호가 존재하지 않으면 예외 발생")
    void notExistOrderTableIdByNumberOfGuests() {
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(1L, 3));
    }

    @Test
    @DisplayName("빈 테이블의 고객 수는 변경시 예외 발생")
    void notChangeEmptyOrderTable() {
        // given
        final OrderTable emptyOrderTable = new OrderTable.Builder()
                .setId(1L)
                .setGuestNumber(GuestNumber.of(0))
                .setEmpty(true)
                .build();
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(emptyOrderTable));
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(1L, 1));
    }
}
