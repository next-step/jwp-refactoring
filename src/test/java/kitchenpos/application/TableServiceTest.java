package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    private TableService tableService;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderDao, orderTableDao);
    }

    @DisplayName("주문 테이블을 등록한다.")
    @Test
    void createTable() {
        // given
        OrderTable orderTable = new OrderTable(0, true);
        given(orderTableDao.save(any())).willReturn(orderTable);

        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertEquals(orderTable, savedOrderTable);
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void getTables() {
        // given
        List<OrderTable> orderTables = Collections.singletonList(
            new OrderTable(0, true));
        given(orderTableDao.findAll()).willReturn(orderTables);

        // when
        List<OrderTable> findOrderTables = tableService.list();

        // then
        assertThat(orderTables)
            .containsExactlyElementsOf(findOrderTables);
    }

    @DisplayName("주문 테이블의 주문 등록 가능 여부를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        // given
        OrderTable orderTable = new OrderTable(1L, 0, false);
        OrderTable findOrderTable = new OrderTable(1L, 0, true);
        given(orderTableDao.findById(any())).willReturn(Optional.of(findOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(
            any(), any())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(findOrderTable);

        // when
        OrderTable changeTable = tableService.changeEmpty(orderTable.getId(), orderTable);

        // then
        assertFalse(changeTable.isEmpty());
    }

    @DisplayName("주문 테이블이 테이블 그룹에 속해있다면 주문 등록 가능 여부를 변경할 수 없다.")
    @Test
    void changeEmptyExistTableGroup() {
        // given
        OrderTable orderTable = new OrderTable(1L, 1L, 0, true);
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        Long orderTableId = orderTable.getId();

        // when & then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(
            orderTableId, orderTable));

        verify(orderTableDao).findById(any());
        verify(orderTableDao, times(0)).save(any());
    }

    @DisplayName("주문 상태가 계산 완료가 아니면 주문 테이블의 주문 등록 가능 여부를 변경할 수 없다.")
    @Test
    void changeEmptyOrderStatusIsNotCompletion() {
        // given
        OrderTable orderTable = new OrderTable(1L, 0, true);
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(true);
        Long orderTableId = orderTable.getId();

        // when & then
        assertThrows(IllegalArgumentException.class, () ->
            tableService.changeEmpty(orderTableId, orderTable));

        verify(orderTableDao).findById(any());
        verify(orderTableDao, times(0)).save(any());
    }

    @DisplayName("방문한 손님 수를 갱신할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable orderTable = new OrderTable(1L, 5, false);
        OrderTable findOrderTable = new OrderTable(1L, 0, false);
        given(orderTableDao.findById(any())).willReturn(Optional.of(findOrderTable));
        given(orderTableDao.save(any())).willReturn(findOrderTable);
        // when
        OrderTable savedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);

        // then
        assertEquals(5, savedOrderTable.getNumberOfGuests());
    }

    @DisplayName("0명 이하로는 방문한 손님 수를 갱신할 수 없다.")
    @Test
    void changeNumberOfGuestsLessThanZero() {
        // given
        OrderTable orderTable = new OrderTable(1L, -1, false);
        Long orderTableId = orderTable.getId();

        // when & then
        assertThrows(IllegalArgumentException.class, () ->
            tableService.changeNumberOfGuests(orderTableId, orderTable));
        verify(orderTableDao, times(0)).findById(any());
    }

    @DisplayName("빈 테이블일 경우 방문한 손님 수를 갱신할 수 없다.")
    @Test
    void changeNumberOfGuestsEmptyTable() {
        // given
        OrderTable orderTable = new OrderTable(1L, 0, true);
        Long orderTableId = orderTable.getId();
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));

        // when & then
        assertThrows(IllegalArgumentException.class, () ->
            tableService.changeNumberOfGuests(orderTableId, orderTable));
        verify(orderTableDao).findById(any());
    }
}
