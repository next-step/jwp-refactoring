package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Or;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("생성 테스트")
    @Test
    public void create() {
        OrderTable mockOrderTable = mock(OrderTable.class);
        given(orderTableDao.save(any()))
                .willReturn(mockOrderTable);

        OrderTable result = tableService.create(new OrderTable());
        assertThat(result).isEqualTo(mockOrderTable);
    }

    @DisplayName("테이블 상태 변경 - 비어있도록")
    @Test
    public void changeStatusToEmpty() {
        OrderTable mockOrderTable = mock(OrderTable.class);
        given(orderTableDao.findById(any()))
                .willReturn(Optional.of(mockOrderTable));
        given(mockOrderTable.getTableGroupId())
                .willReturn(null);
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any()))
                .willReturn(false);

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        tableService.changeEmpty(1l, orderTable);

        verify(mockOrderTable, times(1))
                .setEmpty(true);
    }

    @DisplayName("테이블 상태 변경 불가능 케이스 1 - 테이블의 ID가 잘못된 경우")
    @Test
    public void invalidCase1() {
        assertThatThrownBy(() -> {
            tableService.changeEmpty(9999999l, new OrderTable());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 상태 변경 불가능 케이스 2 - 테이블이 테이블 그룹에 속해있는 경우")
    @Test
    public void invalidCase2() {
        OrderTable mockOrderTable = mock(OrderTable.class);
        given(orderTableDao.findById(any()))
                .willReturn(Optional.of(mockOrderTable));
        given(mockOrderTable.getTableGroupId())
                .willReturn(1l);
        assertThatThrownBy(() -> {
            tableService.changeEmpty(1l, new OrderTable());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 상태 변경 불가능 케이스 3 - 테이블의 상태가 유효하지 않은 경우")
    @Test
    public void invalidCase3() {
        OrderTable mockOrderTable = mock(OrderTable.class);
        given(orderTableDao.findById(any()))
                .willReturn(Optional.of(mockOrderTable));
        given(mockOrderTable.getTableGroupId())
                .willReturn(null);
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any()))
                .willReturn(true);
        assertThatThrownBy(() -> {
            tableService.changeEmpty(1l, new OrderTable());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 인원수를 변경")
    @Test
    public void changeNumberOfGuest() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(10);

        OrderTable mockOrderTable = mock(OrderTable.class);
        given(orderTableDao.findById(any()))
                .willReturn(Optional.of(mockOrderTable));
        given(mockOrderTable.isEmpty())
                .willReturn(false);

        tableService.changeNumberOfGuests(1l, orderTable);
        verify(mockOrderTable, times(1))
                .setNumberOfGuests(10);
    }

    @DisplayName("테이블의 손님 수를 변경할 수 없는 경우 - 손님 수가 유효하지 않을때")
    @Test
    public void invalidChangeCase1() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(1l, orderTable);
        }).isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("테이블의 손님 수를 변경할 수 없는 경우 - 테이블이 비어있는 상태인 경우")
    @Test
    public void invalidChangeCase2() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);

        OrderTable mockOrderTable = mock(OrderTable.class);
        given(orderTableDao.findById(any()))
                .willReturn(Optional.of(mockOrderTable));
        given(mockOrderTable.isEmpty())
                .willReturn(true);

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(1l, orderTable);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수를 변경할 수 없는 경우 - 테이블의 ID가 잘못된 경우")
    @Test
    public void invalidChangeCase3() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);

        given(orderTableDao.findById(1l))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(1l, orderTable);
        }).isInstanceOf(IllegalArgumentException.class);
    }

}
