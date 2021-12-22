package kitchenpos.table;

import kitchenpos.application.TableService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("테이블 관련 기능")
class TableTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Test
    @DisplayName("테이블을 생성할 수 있다.")
    void createTable() {
        // given
        when(orderTableDao.save(any())).thenReturn(new OrderTable(1L, null, 0, true));
        OrderTable orderTable = mock(OrderTable.class);

        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertAll(
                () -> assertThat(savedOrderTable.getId()).isEqualTo(1L),
                () -> assertThat(savedOrderTable.getTableGroupId()).isNull(),
                () -> assertThat(savedOrderTable.getNumberOfGuests()).isZero(),
                () -> assertThat(savedOrderTable.isEmpty()).isTrue(),
                () -> verify(orderTable, times(1)).setTableGroupId(null)
        );
    }

    @Test
    @DisplayName("테이블 목록을 조회할 수 있다.")
    void findTable() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(1L, null, 0, true),
                new OrderTable(2L, null, 0, true)
        );
        when(orderTableDao.findAll()).thenReturn(orderTables);

        // when
        List<OrderTable> findByTables = tableService.list();

        // then
        assertThat(findByTables).extracting("id").contains(1L,2L);
    }

    @Test
    @DisplayName("사용여부를 변경하고자 하는 테이블이 존재하지 않으면 예외가 발생한다.")
    void isNotExistTable() {
        // given
        when(orderTableDao.findById(any())).thenReturn(Optional.empty());

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableService.changeEmpty(1L, new OrderTable());
        });
    }

    @Test
    @DisplayName("사용여부를 변경하고자 하는 테이블이 단체 지정 되어 있다면 예외가 발생한다.")
    void isTableGroup() {
        // given
        when(orderTableDao.findById(any())).thenReturn(Optional.of(new OrderTable(1L, 1L, 0, true)));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableService.changeEmpty(1L, new OrderTable());
        });
    }

    @Test
    @DisplayName("주문상태가 조리 또는 식사면 예외가 발생한다.")
    void orderStatusCookingOrMeal() {
        // given
        when(orderTableDao.findById(any())).thenReturn(Optional.of(new OrderTable(1L, null, 0, true)));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).thenReturn(true);

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableService.changeEmpty(1L, new OrderTable());
        });
    }

    @Test
    @DisplayName("테이블의 사용여부를 변경할 수 있다.")
    void changeEmpty() {
        // given
        when(orderTableDao.findById(any())).thenReturn(Optional.of(new OrderTable(1L, null, 0, true)));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).thenReturn(false);
        when(orderTableDao.save(any())).thenReturn(new OrderTable(1L, null, 0, false));
        OrderTable orderTable = mock(OrderTable.class);

        // when
        OrderTable savedOrderTable = tableService.changeEmpty(1L, orderTable);

        // then
        assertAll(
                () -> assertThat(savedOrderTable.getId()).isOne(),
                () -> assertThat(savedOrderTable.getTableGroupId()).isNull(),
                () -> assertThat(savedOrderTable.getNumberOfGuests()).isZero(),
                () -> assertThat(savedOrderTable.isEmpty()).isFalse(),
                () -> verify(orderTable, times(1)).isEmpty()
        );
    }

    @Test
    @DisplayName("방문한 손님 수가 0명 미만일 시 예외가 발생한다.")
    void numberOfGuestLessThanZero() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableService.changeNumberOfGuests(1L, new OrderTable(-1));
        });
    }

    @Test
    @DisplayName("방문한 손님 수를 변경하고자 하는 테이블이 존재하지 않으면 예외가 발생한다.")
    void changeNumberOfGuestsIsNotExistTable() {
        // given
        when(orderTableDao.findById(any())).thenReturn(Optional.empty());

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableService.changeNumberOfGuests(1L, new OrderTable(0));
        });
    }

    @Test
    @DisplayName("방문한 손님 수를 변경하고자 하는 테이블이 사용불가할 경우 예외가 발생한다.")
    void changeNumberOfGuestsIsEmptyTable() {
        // given
        when(orderTableDao.findById(any())).thenReturn(Optional.of(new OrderTable(true)));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableService.changeNumberOfGuests(1L, new OrderTable(0));
        });
    }

    @Test
    @DisplayName("방문한 손님 수를 변경할 수 있다.")
    void changeNumberOfGuests() {
        // given
        when(orderTableDao.findById(any())).thenReturn(Optional.of(new OrderTable(1L, null, 0, false)));
        when(orderTableDao.save(any())).thenReturn(new OrderTable(1L, null, 5, false));

        // when
        OrderTable savedOrderTable = tableService.changeNumberOfGuests(1L, new OrderTable(5));

        // then
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(5);
    }
}
