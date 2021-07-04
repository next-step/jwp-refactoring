package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블을 등록한다.")
    @Test
    void create() {
        OrderTable orderTable = new OrderTable(1L, 1L, 2, true);
        given(orderTableDao.save(any())).willReturn(orderTable);

        OrderTable created = tableService.create(orderTable);

        assertThat(created).isEqualTo(orderTable);
        assertThat(created.getTableGroupId()).isNull();
    }

    @DisplayName("주문 테이블 리스트를 조회한다.")
    @Test
    void list() {
        OrderTable orderTable1 = new OrderTable(1L, 1L, 2, true);
        OrderTable orderTable2 = new OrderTable(2L, 2L, 4, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        given(orderTableDao.findAll()).willReturn(orderTables);

        List<OrderTable> findedTables = tableService.list();

        assertThat(findedTables).containsExactly(orderTable1, orderTable2);
    }

    @DisplayName("주문 테이블 empty 값을 변경한다.")
    @ParameterizedTest
    @CsvSource(value = {"false:true", "true:false"}, delimiter = ':')
    void changeEmpty(boolean preEmptyValue, boolean afterEmptyValue) {
        OrderTable orderTable = new OrderTable(1L, null, 2, preEmptyValue);
        OrderTable changeTable = new OrderTable(1L, null, 2, afterEmptyValue);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderTableDao.save(any())).willReturn(changeTable);

        OrderTable changed = tableService.changeEmpty(orderTable.getId(), changeTable);

        assertThat(changed.isEmpty()).isEqualTo(changeTable.isEmpty());
    }

    @DisplayName("주문 테이블 empty 값을 변경 실패한다. - 변경하려는 주문 테이블이 기존에 등록되지 않은 테이블이면 변경 실패")
    @Test
    void fail_changeEmpty1() {
        OrderTable orderTable = new OrderTable(1L, null, 2, true);
        OrderTable changeTable = new OrderTable(1L, null, 2, false);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), changeTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 empty 값을 변경 실패한다. - 변경하려는 주문 테이블이 테이블 그룹으로 지정되어 있으면 변경 실패")
    @Test
    void fail_changeEmpty2() {
        OrderTable orderTable = new OrderTable(1L, 1L, 2, true);
        OrderTable changeTable = new OrderTable(1L, null, 2, false);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), changeTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 empty 값을 변경 실패한다. - 변경하려는 주문 테이블의 상태가 조리 또는 식사 중일 경우 변경 실패")
    @Test
    void fail_changeEmpty3() {
        OrderTable orderTable = new OrderTable(1L, null, 2, true);
        OrderTable changeTable = new OrderTable(1L, null, 2, false);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), changeTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 guests(손님) 숫자값을 변경한다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = new OrderTable(1L, null, 2, false);
        OrderTable changeTable = new OrderTable(1L, null, 4, false);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderTableDao.save(any())).willReturn(changeTable);

        OrderTable changed = tableService.changeNumberOfGuests(orderTable.getId(), changeTable);

        assertThat(changed.getNumberOfGuests()).isEqualTo(changeTable.getNumberOfGuests());
    }

    @DisplayName("주문 테이블 guests 숫자값 변경 실패한다 - 변경하려는 주문 테이블의 기존 guests 숫자가 0보다 작을 경우 변경 실패")
    @Test
    void fail_changeNumberOfGuests1() {
        OrderTable orderTable = new OrderTable(1L, null, -1, false);
        OrderTable changeTable = new OrderTable(1L, null, 4, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), changeTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 guests 숫자값 변경 실패한다 - 변경하려는 주문 테이블이 기존에 등록되지 않은 테이블이면 변경 실패")
    @Test
    void fail_changeNumberOfGuests2() {
        OrderTable orderTable = new OrderTable(1L, null, 2, false);
        OrderTable changeTable = new OrderTable(1L, null, 4, false);
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), changeTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 guests 숫자값 변경 실패한다 - 변경하려는 주문 테이블의 empty 값이 true이면 변경 실패")
    @Test
    void fail_changeNumberOfGuests3() {
        OrderTable orderTable = new OrderTable(1L, null, 2, true);
        OrderTable changeTable = new OrderTable(1L, null, 4, false);
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), changeTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
