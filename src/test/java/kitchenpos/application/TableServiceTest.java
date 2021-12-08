package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
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
    void 주문_테이블_생성() {
        // given
        OrderTable orderTable = orderTable(1L, null, 0, false);
        when(orderTableDao.save(any(OrderTable.class))).thenReturn(orderTable);

        // when
        final OrderTable actual = tableService.create(orderTable);

        // then
        assertThat(actual).isEqualTo(orderTable);
    }

    @Test
    void 방문한_손님수_입력() {
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
    void 방문한_손님수_입력_주문테이블_미존재() {
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
    void 방문한_손님수_오류() {
        // given
        final OrderTable orderTable = orderTable(1L, null, -1, false);
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));

        // when
        assertThatThrownBy(() -> {
            OrderTable actual = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 방문한_손님수_입력_테이블_손님_존재() {
        // given
        final OrderTable orderTable = orderTable(1L, null, 5, true);
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));

        // when
        assertThatThrownBy(() -> {
            OrderTable actual = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블_조회() {
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

    @Test
    void 주문_테이블_비우기() {
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
    void 주문_테이블_비우기_그룹_테이블_오류() {
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
    void 주문_테이블_비우기_조리중_오류() {
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