package kitchenpos.application.table;

import kitchenpos.table.application.TableService;
import kitchenpos.dao.order.OrderDao;
import kitchenpos.dao.table.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    OrderDao orderDao;
    @Mock
    OrderTableDao orderTableDao;
    @InjectMocks
    TableService tableService;

    @DisplayName("주문 테이블을 등록할 수 있다.")
    @Test
    void createTableTest() {
        OrderTable expected = new OrderTable(1L, 1L, 3, false);
        given(orderTableDao.save(any())).willReturn(expected);

        OrderTable saved = tableService.create(expected);

        assertThat(saved).isEqualTo(expected);
    }

    @DisplayName("주문 테이블의 목록을 조회할 수 있다.")
    @Test
    void tableListTest() {
        OrderTable table1 = new OrderTable(1L, 1L, 3, false);
        OrderTable table2 = new OrderTable(2L, 1L, 4, false);
        given(orderTableDao.findAll()).willReturn(
                Arrays.asList(table1, table2)
        );

        List<OrderTable> results = tableService.list();

        assertThat(results).contains(table1, table2);
    }

    @DisplayName("빈 테이블 설정")
    @Test
    void setTableEmptyTest() {
        OrderTable expected = new OrderTable(1L, null, 3, false);
        OrderTable changed = new OrderTable(1L, null, 3, true);
        given(orderTableDao.findById(any())).willReturn(Optional.of(expected));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(changed);

        OrderTable saved = tableService.changeEmpty(1L, expected);

        assertThat(saved).isEqualTo(changed);
        assertThat(saved.isEmpty()).isTrue();
    }

    @DisplayName("빈 테이블 해지")
    @Test
    void setTableNotEmptyTest() {
        OrderTable expected = new OrderTable(1L, null, 3, true);
        given(orderTableDao.findById(any())).willReturn(Optional.of(expected));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);
        expected.setEmpty(false);
        given(orderTableDao.save(any())).willReturn(expected);

        OrderTable saved = tableService.changeEmpty(1L, expected);

        assertThat(saved).isEqualTo(expected);
        assertThat(saved.isEmpty()).isFalse();
    }

    @DisplayName("단체 지정된 주문 테이블은 빈 테이블 설정/해지할 수 없다.")
    @Test
    void groupTableCantSetEmptyTest() {
        OrderTable expected = new OrderTable(1L, 1L, 3, true);
        given(orderTableDao.findById(any())).willReturn(Optional.of(expected));

        assertThatThrownBy(() -> tableService.changeEmpty(1L, expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 조리/식사인 테이블은 빈 테이블 설정/해지할 수 없다.")
    @Test
    void cookingCantSetEmptyTest() {
        OrderTable expected = new OrderTable(1L, null, 3, true);

        assertThatThrownBy(() -> tableService.changeEmpty(1L, expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수를 입력")
    @Test
    void setCustomerCountTest() {
        OrderTable expected = new OrderTable(1L, null, 3, false);
        given(orderTableDao.findById(any())).willReturn(Optional.of(expected));
        given(orderTableDao.save(any())).willReturn(expected);

        OrderTable saved = tableService.changeNumberOfGuests(1L, expected);

        assertThat(saved.getNumberOfGuests()).isEqualTo(3);

    }

    @DisplayName("방문한 손님 수가 0명 이하이면 손님수를 입력할 수 없다.")
    @Test
    void underZeroCantSetCustomerCountTest() {
        OrderTable expected = new OrderTable(1L, null, -3, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블은 방문한 손님 수를 입력할 수 없다.")
    @Test
    void emptyTableCSetCustomerCountTest() {
        OrderTable expected = new OrderTable(1L, null, -3, true);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

}