package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;

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
        OrderTableRequest orderTableRequest = new OrderTableRequest(1);

        // when
        tableService.create(orderTableRequest);

        // then
        verify(orderTableDao).save(any(OrderTable.class));
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
        OrderTableRequest orderTableRequest = new OrderTableRequest();
        OrderTable savedOrderTable = new OrderTable();
        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(savedOrderTable));

        // when
        tableService.changeEmpty(orderTableId, orderTableRequest);

        // then
        verify(orderTableDao).save(savedOrderTable);
    }

    @Test
    void given_NotExistOrderTable_when_changeEmpty_then_ThrownException() {
        // given
        Long notExistId = 1L;
        OrderTableRequest orderTableRequest = new OrderTableRequest();
        given(orderTableDao.findById(notExistId)).willReturn(Optional.empty());

        // when
        final Throwable notExistIdException = catchThrowable(() -> tableService.changeEmpty(notExistId, orderTableRequest));

        // then
        assertThat(notExistIdException).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void given_OrderTableHasTableGroupId_when_changeEmpty_then_ThrownException() {
        // given
        Long orderTableId = 1L;
        OrderTableRequest orderTableRequest = new OrderTableRequest();
        final OrderTable notNullTableGroupId = new OrderTable();
        notNullTableGroupId.setTableGroupId(1L);
        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(notNullTableGroupId));

        // when
        final Throwable notNullTableGroupIdException = catchThrowable(() -> tableService.changeEmpty(orderTableId, orderTableRequest));

        // then
        assertThat(notNullTableGroupIdException).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests() {
        // given
        Long orderTableId = 1L;
        OrderTableRequest orderTableRequest = new OrderTableRequest(1);
        OrderTable savedOrderTable = new OrderTable();
        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(savedOrderTable));

        // when
        tableService.changeNumberOfGuests(orderTableId, orderTableRequest);

        // then
        verify(orderTableDao).save(savedOrderTable);
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests());
    }

    @Test
    void given_InvalidNumberOfGuests_when_ChangeNumberOfGuests_then_ThrownException() {
        // given
        Long orderTableId = 1L;
        OrderTableRequest minusGuestsOrderTableRequest = new OrderTableRequest();

        // when
        final Throwable invalidNumberOfGuests = catchThrowable(() -> tableService.changeNumberOfGuests(orderTableId, minusGuestsOrderTableRequest));

        // then
        assertThat(invalidNumberOfGuests).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void given_PersistedOrderTableHasEmpty_when_ChangeNumberOfGuests_then_ThrownException() {
        // given
        Long orderTableId = 1L;
        OrderTableRequest orderTableRequest = new OrderTableRequest(1);
        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setEmpty(true);
        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(savedOrderTable));


        // when
        final Throwable emptyOrderTable = catchThrowable(() -> tableService.changeNumberOfGuests(orderTableId, orderTableRequest));

        // then
        assertThat(emptyOrderTable).isInstanceOf(IllegalArgumentException.class);
    }
}
