package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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

    @InjectMocks
    private TableService tableService;

    @Test
    void create() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(1L);

        // when
        tableService.create(orderTable);

        // then
        verify(orderTableDao).save(orderTable);
        assertThat(orderTable.getTableGroupId()).isEqualTo(null);
    }

    @Test
    void list() {
        // when
        tableService.list();

        // then
        verify(orderTableDao).findAll();
    }

    @Test
    void changeEmpty() {
        // given
        Long orderTableId = 1L;
        OrderTable orderTable = new OrderTable();
        OrderTable savedOrderTable = new OrderTable();
        when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(savedOrderTable));

        // when
        tableService.changeEmpty(orderTableId, orderTable);

        // then
        verify(orderTableDao).save(savedOrderTable);
    }

    @Test
    void given_NotExistOrderTable_when_changeEmpty_then_ThrownException() {
        // given
        Long notExistId = 1L;
        OrderTable orderTable = new OrderTable();
        when(orderTableDao.findById(notExistId)).thenReturn(Optional.empty());

        // when
        final Throwable notExistIdException = catchThrowable(() -> tableService.changeEmpty(notExistId, orderTable));

        // then
        assertThat(notExistIdException).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void given_OrderTableHasTableGroupId_when_changeEmpty_then_ThrownException() {
        // given
        Long orderTableId = 1L;
        OrderTable orderTable = new OrderTable();
        final OrderTable notNullTableGroupId = new OrderTable();
        notNullTableGroupId.setTableGroupId(1L);
        when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(notNullTableGroupId));

        // when
        final Throwable notNullTableGroupIdException = catchThrowable(() -> tableService.changeEmpty(orderTableId, orderTable));

        // then
        assertThat(notNullTableGroupIdException).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests() {
        // given
        Long orderTableId = 1L;
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);
        OrderTable savedOrderTable = new OrderTable();
        when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(savedOrderTable));

        // when
        tableService.changeNumberOfGuests(orderTableId, orderTable);

        // then
        verify(orderTableDao).save(savedOrderTable);
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }

    @Test
    void given_InvalidNumberOfGuests_when_ChangeNumberOfGuests_then_ThrownException() {
        // given
        Long orderTableId = 1L;
        OrderTable minusGuestsOrderTable = new OrderTable();
        minusGuestsOrderTable.setNumberOfGuests(-1);

        // when
        final Throwable invalidNumberOfGuests = catchThrowable(() -> tableService.changeNumberOfGuests(orderTableId, minusGuestsOrderTable));

        // then
        assertThat(invalidNumberOfGuests).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void given_PersistedOrderTableHasEmpty_when_ChangeNumberOfGuests_then_ThrownException() {
        // given
        Long orderTableId = 1L;
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);
        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setEmpty(true);
        when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(savedOrderTable));


        // when
        final Throwable emptyOrderTable = catchThrowable(() -> tableService.changeNumberOfGuests(orderTableId, orderTable));

        // then
        assertThat(emptyOrderTable).isInstanceOf(IllegalArgumentException.class);
    }
}
