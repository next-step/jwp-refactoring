package kitchenpos.table.application;

import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    private OrderTable notEmptyTable;
    private OrderTableRequest orderTableRequest;
    private OrderTable emptyTable;
    private List<OrderTable> orderTables = new ArrayList<>();

    private TableService tableService;

    @Mock
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    void setUp() {
        notEmptyTable = new OrderTable(1L,1, false);
        orderTableRequest = new OrderTableRequest(1, false);
        emptyTable = new OrderTable(2L,2, true);
        orderTables.add(notEmptyTable);
        orderTables.add(emptyTable);

        tableService = new TableService(orderTableRepository);
    }

    @DisplayName("주문테이블 등록 테스트")
    @Test
    void createOrderTable() {
        when(orderTableRepository.save(any())).thenReturn(notEmptyTable);

        OrderTableResponse resultOrderTable = tableService.create(orderTableRequest);

        assertAll(
                () -> assertThat(resultOrderTable.getNumberOfGuests()).isEqualTo(1),
                () -> assertThat(resultOrderTable.isEmpty()).isEqualTo(false)
        );
    }

    @DisplayName("주문테이블 등록 예외 테스트: orderTable 객체 null일경우")
    @Test
    void createNullOrderTable() {
        assertThatThrownBy(() -> tableService.create(null))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("주문테이블 목록 조회 테스트")
    @Test
    void findOrderTableList() {
        when(orderTableRepository.findAll()).thenReturn(orderTables);
        List<OrderTableResponse> resultOrderTables = tableService.list();
        List<Integer> resultOrderTableNumOfGuests = resultOrderTables.stream()
                .map(resultOrderTable ->resultOrderTable.getNumberOfGuests())
                .collect(Collectors.toList());
        List<Integer> OrderTableNumOfGuests = orderTables.stream()
                .map(orderTable -> orderTable.getNumberOfGuests())
                .collect(Collectors.toList());

        assertThat(resultOrderTableNumOfGuests).containsExactlyElementsOf(OrderTableNumOfGuests);
    }

    @DisplayName("손님 숫자 변경 테스트")
    @Test
    void changeNumberOfGuestTest() {
        when(orderTableRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(notEmptyTable));

        OrderTableResponse resultOrderTable = tableService.changeNumberOfGuests(1L, 10);

        assertThat(resultOrderTable.getNumberOfGuests()).isEqualTo(10);
    }

    @DisplayName("손님수 변경 예외테스트: 테이블 인원이 0보다 작은경우")
    @Test
    void invalidGuestNum() {
        when(orderTableRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(notEmptyTable));
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> tableService.changeNumberOfGuests(1L, -3)
        );
        assertThat(exception.getMessage()).isEqualTo("손님 수가 올바르지 않습니다.");
    }

    @DisplayName("손님수 변경 예외테스트: 테이블이 조회 안되는 경우")
    @Test
    void notFoundTable() {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> tableService.changeNumberOfGuests(11L, 10)
        );
        assertThat(exception.getMessage()).isEqualTo("테이블을 찾을 수 없습니다.");
    }

    @DisplayName("손님수 변경 예외테스트: 테이블이 비어있는경우")
    @Test
    void EmptyTable() {
        when(orderTableRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(emptyTable));

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> tableService.changeNumberOfGuests(2L, 10)
        );
        assertThat(exception.getMessage()).isEqualTo("테이블이 비어있습니다.");
    }

    @DisplayName("테이블 상태 변경 테스트")
    @Test
    void changeEmptyTest() {
        when(orderTableRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(notEmptyTable));

        OrderTableResponse resultTable = tableService.changeEmpty(1L, true);

        assertThat(resultTable.isEmpty()).isEqualTo(true);
    }

    @DisplayName("테이블 상태 변경 예외테스트: 그룹핑된 테이블")
    @Test
    void existTableGroup() {
        when(orderTableRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(notEmptyTable));
        notEmptyTable.addTableGroup(1L);

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> tableService.changeEmpty(1L, true)
        );
        assertThat(exception.getMessage()).isEqualTo("그룹핑된 상태입니다.");
    }

    @DisplayName("테이블 상태 변경 예외테스트: 완료되지않은 테이블 상태")
    @Test
    void unCompleteTableStatus() {
        when(orderTableRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(notEmptyTable));
        Order order = new Order(notEmptyTable, new ArrayList<>());

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> tableService.changeEmpty(1L, true)
        );
        assertThat(exception.getMessage()).isEqualTo("주문이 완료되지 않았습니다.");
    }
}
