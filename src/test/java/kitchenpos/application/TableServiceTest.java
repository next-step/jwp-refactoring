package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    private TableService tableService;

    @BeforeEach
    void beforeEach() {
        tableService = new TableService(orderDao, orderTableDao);
    }

    @Test
    @DisplayName("주문 생성")
    void create() {
        final OrderTable givenOrderTable = new OrderTable(null, null, 3, true);
        final OrderTable savedOrderTable = new OrderTable(1L, 1L, 3, false);

        when(orderTableDao.save(any(OrderTable.class)))
                .thenReturn(savedOrderTable);
        final OrderTable actual = tableService.create(givenOrderTable);

        assertThat(actual).isEqualTo(savedOrderTable);
    }

    @Test
    @DisplayName("모든 주문을 조회한다")
    void list() {
        final OrderTable givenOrderTable1 = new OrderTable(1L, 1L, 3, false);
        final OrderTable givenOrderTable2 = new OrderTable(2L, 1L, 3, false);

        when(orderTableDao.findAll())
                .thenReturn(Arrays.asList(givenOrderTable1, givenOrderTable2));

        List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).containsAll(Arrays.asList(givenOrderTable1, givenOrderTable2));
    }

    @Test
    @DisplayName("주문 테이블을 비어있음 상태로 변경한다.")
    void change_order_empty() {
        final Long givenOrderTableId = 1L;
        final OrderTable givenOrderTable = new OrderTable(1L, null, 3, true);
        final OrderTable savedOrderTable = new OrderTable(2L, null, 3, false);
        final OrderTable updatedOrderTable = new OrderTable(1L, null, 3, true);
        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.of(savedOrderTable));
        when(orderTableDao.save(any(OrderTable.class)))
                .thenReturn(updatedOrderTable);

        final OrderTable actual = tableService.changeEmpty(givenOrderTableId, givenOrderTable);

        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("주문 테이블을 비울 시 테이블 그룹이 포함된 경우 예외를 던진다.")
    void change_order_empty_with_group_table() {
        final Long givenOrderTableId = 1L;
        final OrderTable givenOrderTable = new OrderTable(1L, null, 3, true);
        final OrderTable savedOrderTable = new OrderTable(2L, 1L, 3, false);
        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.of(savedOrderTable));

        assertThatThrownBy(() -> tableService.changeEmpty(givenOrderTableId, givenOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("방문 손님 수를 변경한다.")
    void change_number_of_guests() {
        Long givenOrderTableId = 1L;
        final OrderTable givenOrderTable = new OrderTable(null, null, 3, true);
        final OrderTable savedOrderTable = new OrderTable(1L, 1L, 3, false);
        final OrderTable updatedOrderTable = new OrderTable(1L, 1L, 5, false);
        int updateNumberOfGuest = 5;
        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.of(savedOrderTable));
        when(orderTableDao.save(savedOrderTable))
                .thenReturn(updatedOrderTable);

        OrderTable actual = tableService.changeNumberOfGuests(givenOrderTableId, givenOrderTable);

        assertThat(actual).extracting("numberOfGuests").isEqualTo(updateNumberOfGuest);
    }

    @Test
    @DisplayName("손님 수 변경시, 방문 손님 수가 없으면 예외를 던진다.")
    void change_with_empty_number_of_guests() {
        Long givenOrderTableId = 1L;
        final OrderTable givenOrderTable = new OrderTable(null, null, -1, true);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(givenOrderTableId, givenOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어있는 테이블의 손님 수 변경시 예외를 던진다.")
    void change_number_of_guest_with_empty_order_table() {
        Long givenOrderTableId = 1L;
        final OrderTable givenOrderTable = new OrderTable(null, null, 5, true);
        final OrderTable savedOrderTable = new OrderTable(1L, 1L, 3, true);

        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.of(savedOrderTable));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(givenOrderTableId, givenOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
