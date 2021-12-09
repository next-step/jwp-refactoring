package kitchenpos.application;

import static kitchenpos.application.sample.OrderTableSample.emptyTwoGuestsOrderTable;
import static kitchenpos.application.sample.OrderTableSample.notEmptyFiveGuestsOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("테이블 서비스")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("테이블을 등록할 수 있다.")
    void create() {
        //given
        OrderTable request = orderTableCreateRequest(5, true);

        //when
        tableService.create(request);

        //then
        ArgumentCaptor<OrderTable> orderTableCaptor = ArgumentCaptor.forClass(OrderTable.class);
        verify(orderTableDao, only()).save(orderTableCaptor.capture());
        assertThat(orderTableCaptor.getValue())
            .extracting(
                OrderTable::getTableGroupId, OrderTable::getNumberOfGuests, OrderTable::isEmpty)
            .containsExactly(null, request.getNumberOfGuests(), request.isEmpty());
    }

    @Test
    @DisplayName("테이블들을 조회할 수 있다.")
    void list() {
        //when
        tableService.list();

        //then
        verify(orderTableDao, only()).findAll();
    }

    @Test
    @DisplayName("테이블의 상태를 변경할 수 있다.")
    void changeEmpty() {
        //given
        OrderTable request = emptyUpdateRequest(true);

        when(orderTableDao.findById(anyLong()))
            .thenReturn(Optional.of(notEmptyFiveGuestsOrderTable()));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(false);

        //when
        tableService.changeEmpty(1L, request);

        //then
        ArgumentCaptor<OrderTable> orderTableCaptor = ArgumentCaptor.forClass(OrderTable.class);
        verify(orderTableDao, times(1)).save(orderTableCaptor.capture());
        assertThat(orderTableCaptor.getValue())
            .extracting(OrderTable::isEmpty)
            .isEqualTo(request.isEmpty());
    }

    @Test
    @DisplayName("빈테이블 여부를 변경하려면 변경하려는 테이블의 정보가 반드시 저장되어 있어야 한다.")
    void changeEmpty_notExistOrderTable_thrownException() {
        //given
        OrderTable request = emptyUpdateRequest(false);
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.empty());

        //when
        ThrowingCallable changeCallable = () -> tableService.changeEmpty(1L, request);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(changeCallable);
    }

    @Test
    @DisplayName("빈테이블 여부를 변경하려면 그룹이 지정되어 있지 않아야 한다.")
    void changeEmpty_existTableGroupId_thrownException() {
        //given
        OrderTable request = emptyUpdateRequest(false);

        OrderTable notEmptyOrderTable = notEmptyFiveGuestsOrderTable();
        notEmptyOrderTable.setTableGroupId(1L);
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(notEmptyOrderTable));

        //when
        ThrowingCallable changeCallable = () -> tableService.changeEmpty(1L, request);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(changeCallable);
    }

    @Test
    @DisplayName("빈테이블 여부를 변경하려면 주문 상태가 조리 또는 식사 중이라면 변경이 불가능하다.")
    void changeEmpty_cookOrMealStatus_thrownException() {
        //given
        OrderTable request = emptyUpdateRequest(false);

        when(orderTableDao.findById(anyLong()))
            .thenReturn(Optional.of(notEmptyFiveGuestsOrderTable()));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(true);

        //when
        ThrowingCallable changeCallable = () -> tableService.changeEmpty(1L, request);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(changeCallable);
    }

    @Test
    @DisplayName("방문한 손님 수를 변경할 수 있다.")
    void changeNumberOfGuests() {
        //given
        OrderTable request = numberOfGuestsUpdateRequest(3);

        when(orderTableDao.findById(anyLong()))
            .thenReturn(Optional.of(notEmptyFiveGuestsOrderTable()));

        //when
        tableService.changeNumberOfGuests(1L, request);

        //then
        ArgumentCaptor<OrderTable> orderTableCaptor = ArgumentCaptor.forClass(OrderTable.class);
        verify(orderTableDao, times(1)).save(orderTableCaptor.capture());
        assertThat(orderTableCaptor.getValue())
            .extracting(OrderTable::getNumberOfGuests)
            .isEqualTo(request.getNumberOfGuests());
    }

    @Test
    @DisplayName("변경하려는 손님 수는 0이상 이어야 한다.")
    void changeNumberOfGuests_lessThanZero_thrownException() {
        //given
        OrderTable request = numberOfGuestsUpdateRequest(-1);

        //when
        ThrowingCallable changeCallable = () -> tableService.changeNumberOfGuests(1L, request);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(changeCallable);
    }

    @Test
    @DisplayName("변경하려는 테이블의 정보가 반드시 저장되어 있어야 한다.")
    void changeNumberOfGuests_notExistOrderTable_thrownException() {
        //given
        OrderTable request = numberOfGuestsUpdateRequest(3);

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.empty());

        //when
        ThrowingCallable changeCallable = () -> tableService.changeNumberOfGuests(1L, request);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(changeCallable);
    }

    @Test
    @DisplayName("변경하려는 테이블은 비어있지 않아야 한다.")
    void changeNumberOfGuests_empty_thrownException() {
        //given
        OrderTable request = numberOfGuestsUpdateRequest(3);

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(emptyTwoGuestsOrderTable()));

        //when
        ThrowingCallable changeCallable = () -> tableService.changeNumberOfGuests(1L, request);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(changeCallable);
    }

    private OrderTable numberOfGuestsUpdateRequest(int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    private OrderTable emptyUpdateRequest(boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        return orderTable;
    }

    private OrderTable orderTableCreateRequest(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }
}
