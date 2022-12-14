package kitchenpos.application;

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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable secondTable;

    @Test
    @DisplayName("주문 테이블 등록 할 수 있다.")
    void createTable() {
        OrderTable 주문테이블 = new OrderTable(1L, null, 4, true);
        when(tableService.create(주문테이블)).thenReturn(주문테이블);

        OrderTable result = tableService.create(주문테이블);

        assertThat(result.getId()).isNotNull();
        assertThat(result.isEmpty()).isTrue();
        assertThat(result.getNumberOfGuests()).isEqualTo(4);

    }

    @Test
    @DisplayName("주문 테이블 리스트를 받을 수 있다.")
    void getTableList() {
        OrderTable 주문테이블_일번 = new OrderTable(1L, null, 4, true);
        OrderTable 주문테이블_이번 = new OrderTable(2L, null, 2, true);

        when(orderTableDao.findAll()).thenReturn(Arrays.asList(주문테이블_일번, 주문테이블_이번));

        List<OrderTable> result = tableService.list();

        assertThat(result).hasSize(2);
        assertThat(result).contains(주문테이블_일번, 주문테이블_이번);
    }

    @Test
    @DisplayName("주문테이블의 상태를 변경할 수 있다.")
    void changeTableStatusEmpty() {
        OrderTable 주문테이블 = new OrderTable(1L, null, 4, false);
        OrderTable 변경할_주문테이블 = new OrderTable(1L, null, 4, true);

        when(orderTableDao.findById(any())).thenReturn(Optional.of(주문테이블));
        when(orderTableDao.save(any())).thenReturn(주문테이블);

        OrderTable changeOrderTable = tableService.changeEmpty(1L, 변경할_주문테이블);

        assertThat(changeOrderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("등록된 주문 테이블이여야 주문 테아블 상태를 변경 할 수 있다.")
    void isRegisterTable() {
        when(orderTableDao.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                tableService.changeEmpty(1L, new OrderTable())
        ).isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("주문 테이블이 단체 지정 석에 속해 있지 않아야한다.")
    void changeTableIsNotTableGroup() {
        OrderTable 주문테이블 = new OrderTable(1L, 1L, 4, false);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(주문테이블));

        assertThatThrownBy(() ->
                tableService.changeEmpty(1L, new OrderTable())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상태가 조리, 식사중 이면 테이블 이용여부 변경이 불가능하다.")
    void changeFailIfStatusCookingAndMeal() {
        OrderTable 주문테이블 = new OrderTable(1L, null, 4, false);

        when(orderTableDao.findById(1L)).thenReturn(Optional.of(주문테이블));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(true);

        assertThatThrownBy(() ->
                tableService.changeEmpty(1L, new OrderTable())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 방문한 손님의 수를 바꿀 수 있다.")
    void notChangeGuestNumber() {
        OrderTable 주문테이블 = new OrderTable(1L, null, 4, false);
        OrderTable 변경할_주문테이블 = new OrderTable(1L, null, 3, false);

        when(orderTableDao.findById(any())).thenReturn(Optional.of(주문테이블));
        when(orderTableDao.save(any())).thenReturn(주문테이블);

        OrderTable changeOrderTable = tableService.changeNumberOfGuests(주문테이블.getId(), 변경할_주문테이블);

        assertThat(changeOrderTable.getNumberOfGuests()).isEqualTo(변경할_주문테이블.getNumberOfGuests());
    }

    @Test
    @DisplayName("방문한 손님의 수는 0명 이상이여야한다.")
    void notChangeGuestIsMinZero() {
        OrderTable 주문테이블 = new OrderTable(1L, null, -1, false);
        assertThatThrownBy(() ->
                tableService.changeNumberOfGuests(1L, 주문테이블)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록된 주문 테이블이여아 한다.")
    void alreadyRegisterTable() {
        when(orderTableDao.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                tableService.changeNumberOfGuests(1L, new OrderTable())
        ).isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("등록된 주문 테이블이여아 주문 테이블의 방문한 손님의 수를 바꿀 수 있다.")
    void tableIsNotEmpty() {
        OrderTable 주문테이블 = new OrderTable(1L, null, 6, true);

        when(orderTableDao.findById(any())).thenReturn(Optional.of(주문테이블));

        assertThatThrownBy(() ->
                tableService.changeNumberOfGuests(1L, new OrderTable())
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
