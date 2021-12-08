package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("테이블 관리")
class TableServiceTest {
    private OrderDao orderDao;
    private OrderTableDao orderTableDao;
    private TableService tableService;

    public static OrderTable orderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    @BeforeEach
    void setUp() {
        orderDao = mock(OrderDao.class);
        orderTableDao = mock(OrderTableDao.class);
        tableService = new TableService(orderDao, orderTableDao);
    }

    @Test
    @DisplayName("주문 테이블 생성")
    void createOrderTable() {
        // given
        OrderTable orderTable = orderTable(1L, null, 0, false);
        when(orderTableDao.save(any(OrderTable.class))).thenReturn(orderTable);

        // when
        final OrderTable actual = tableService.create(orderTable);

        // then
        assertThat(actual).isEqualTo(orderTable);
    }

    @Test
    @DisplayName("주문 테이블 조회")
    void findAllOrderTable() {
        // given
        final OrderTable orderTable = orderTable(1L, null, 4, true);
        final OrderTable orderTable2 = orderTable(2L, null, 6, true);
        when(orderTableDao.findAll()).thenReturn(new ArrayList<>(Arrays.asList(orderTable, orderTable2)));

        // when
        final List<OrderTable> actual = tableService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual).containsExactly(orderTable, orderTable2)
        );
    }

    @Nested
    @DisplayName("방문 손님 관리")
    class ChangeNumberOfGuest {
        @Test
        @DisplayName("성공")
        void changeSuccess() {
            // given
            final OrderTable orderTable = orderTable(1L, null, 3, false);
            when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
            when(orderTableDao.save(any(OrderTable.class))).thenReturn(orderTable);

            // when
            final OrderTable actual = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);

            // then
            assertThat(actual.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
        }

        @Test
        @DisplayName("실패 - 주문 테이블이 미존재")
        void changeFailNotExistsOrderTable() {
            // given
            final OrderTable orderTable = orderTable(1L, null, 5, false);
            when(orderTableDao.findById(anyLong())).thenReturn(Optional.empty());
            when(orderTableDao.save(any(OrderTable.class))).thenReturn(orderTable);

            // when
            assertThatThrownBy(() -> {
                OrderTable actual = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
            }).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("실패 - 손님 숫자 0 미만 오류")
        void changeFailIllegalGuestCount() {
            // given
            final OrderTable orderTable = orderTable(1L, null, -1, false);
            when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));

            // when
            assertThatThrownBy(() -> {
                OrderTable actual = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
            }).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("실패 - 주문 테이블에 손님이 존재")
        void changeFailOrderTableExistsGuest() {
            // given
            final OrderTable orderTable = orderTable(1L, null, 5, true);
            when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));

            // when
            assertThatThrownBy(() -> {
                OrderTable actual = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
            }).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("주문 테이블 비우기")
    class ChangeEmptyOrderTable {
        @Test
        @DisplayName("성공")
        void changeSuccess() {
            // given
            final OrderTable orderTable = orderTable(1L, null, 3, true);
            when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
            when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(false);
            when(orderTableDao.save(any(OrderTable.class))).thenReturn(orderTable);

            // when
            final OrderTable actual = tableService.changeEmpty(orderTable.getId(), orderTable);

            // then
            assertThat(actual.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("실패 - 그룹화 된 주문 테이블은 모두 자리가 비어야함")
        void changeFailOrderTableNotEmpty() {
            // given
            final OrderTable orderTable = orderTable(1L, 1L, 3, true);
            when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
            when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(false);
            when(orderTableDao.save(any(OrderTable.class))).thenReturn(orderTable);

            // when
            assertThatThrownBy(() -> {
                OrderTable actual = tableService.changeEmpty(orderTable.getId(), orderTable);
            }).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("실패 - 비우려는 테이블의 주문상태가 완료되지 않음.")
        void changeFailOrderStatus() {
            // given
            final OrderTable orderTable = orderTable(1L, 1L, 3, true);
            when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
            when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(true);
            when(orderTableDao.save(any(OrderTable.class))).thenReturn(orderTable);

            // when
            assertThatThrownBy(() -> {
                OrderTable actual = tableService.changeEmpty(orderTable.getId(), orderTable);
            }).isInstanceOf(IllegalArgumentException.class);
        }
    }

}
