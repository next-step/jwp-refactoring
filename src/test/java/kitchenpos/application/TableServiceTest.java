package kitchenpos.application;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("TableService 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void createOrderTable() {
        // given
        OrderTable orderTable = new OrderTable(1L, 4, true);
        when(orderTableRepository.save(orderTable)).thenReturn(orderTable);

        // when
        OrderTable result = tableService.create(orderTable);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(orderTable.getId()),
                () -> assertThat(result.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests())
        );
    }

    @DisplayName("주문 테이블을 조회할 수 있다.")
    @Test
    void findAllOrderTable() {
        // given
        OrderTable orderTable = new OrderTable(1L, 4, true);
        when(orderTableRepository.findAll()).thenReturn(Arrays.asList(orderTable));

        // when
        List<OrderTable> results = tableService.list();

        // then
        assertAll(
                () -> assertThat(results).hasSize(1),
                () -> assertThat(results.get(0).getId()).isEqualTo(orderTable.getId()),
                () -> assertThat(results.get(0).getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests())
        );
    }

    @DisplayName("주문 테이블이 비어있는 상태를 변경할 수 있다.")
    @Test
    void updateOrderTableEmpty() {
        // given
        boolean expectedEmpty = false;
        OrderTable orderTable = new OrderTable(1L, 4, true);
        OrderTable updatedTable = new OrderTable(
                orderTable.getId(),
                orderTable.getNumberOfGuests(),
                expectedEmpty
        );

        when(orderTableRepository.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))
        ).thenReturn(false);
        when(orderTableRepository.save(orderTable)).thenReturn(orderTable);


        // when
        OrderTable result = tableService.changeEmpty(orderTable.getId(), updatedTable);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(orderTable.getId()),
                () -> assertThat(result.isEmpty()).isEqualTo(expectedEmpty)
        );
    }

    @DisplayName("등록되지 않은 주문 테이블의 비어있는 상태를 변경하면 예외가 발생한다.")
    @Test
    void updateNotExistOrderTableEmptyException() {
        // given
        OrderTable orderTable = new OrderTable(1L, 4, true);
        when(orderTableRepository.findById(orderTable.getId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정된 주문 테이블의 비어있는 상태를 변경하면 예외가 발생한다.")
    @Test
    void updateGroupTableEmptyException() {
        // given
        OrderTable orderTable = new OrderTable(1L, 4, true);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(orderTable));
        orderTable.setTableGroup(tableGroup);
        when(orderTableRepository.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 조리 or 식사 상태라면 비어있는 상태 변경시 예외가 발생한다.")
    @Test
    void updateWrongOrderStatusEmptyException() {
        // given
        OrderTable orderTable = new OrderTable(1L, 4, true);
        when(orderTableRepository.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))
        ).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경할 수 있다.")
    @Test
    void updateNumberOfGuest() {
        // given
        int expectedGuestNumber = 6;
        OrderTable orderTable = new OrderTable(1L, 4, false);
        OrderTable updatedTable = new OrderTable(
                orderTable.getId(),
                expectedGuestNumber,
                orderTable.isEmpty()
        );

        when(orderTableRepository.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        when(orderTableRepository.save(orderTable)).thenReturn(orderTable);

        // when
        OrderTable result = tableService.changeNumberOfGuests(orderTable.getId(), updatedTable);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(orderTable.getId()),
                () -> assertThat(result.getNumberOfGuests()).isEqualTo(expectedGuestNumber)
        );
    }

    @DisplayName("변경하려는 손님수가 0보다 작으면 예외가 발생한다.")
    @Test
    void guestNumberLowerThanZero() {
        // given
        OrderTable orderTable = new OrderTable(1L, 0, false);
        OrderTable updatedTable = new OrderTable(
                orderTable.getId(),
                -1,
                orderTable.isEmpty()
        );

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), updatedTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 주문 테이블의 손님 수를 변경하면 예외가 발생한다.")
    @Test
    void notExistOrderTableUpdateException() {
        // given
        OrderTable orderTable = new OrderTable(1L, 0, false);
        when(orderTableRepository.findById(orderTable.getId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 주문 테이블에 대해 손님 수를 변경하면 예외가 발생한다.")
    @Test
    void emptyOrderTableUpdateException() {
        // given
        OrderTable orderTable = new OrderTable(1L, 0, true);
        when(orderTableRepository.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
