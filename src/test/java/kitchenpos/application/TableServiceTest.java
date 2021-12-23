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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블을 등록한다.")
    @Test
    void create() {
        final OrderTable orderTable = new OrderTable(1L, null, 0, false);
        given(orderTableDao.save(any())).willReturn(orderTable);

        final OrderTable actual = tableService.create(orderTable);

        assertThat(actual).isEqualTo(orderTable);
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() {
        final OrderTable orderTable1 = new OrderTable(1L, null, 0, false);
        final OrderTable orderTable2 = new OrderTable(2L, null, 0, false);
        final List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        given(orderTableDao.findAll()).willReturn(orderTables);

        final List<OrderTable> actual = tableService.list();

        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual.get(0)).isEqualTo(orderTable1),
                () -> assertThat(actual.get(1)).isEqualTo(orderTable2)
        );
    }

    @DisplayName("주문 테이블의 손님수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        final OrderTable orderTable = new OrderTable(1L, 1L, 0, false);
        final OrderTable changeOrderTable = new OrderTable(1L, 1L, 10, false);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderTableDao.save(any())).willReturn(changeOrderTable);

        final OrderTable actual = tableService.changeNumberOfGuests(1L, changeOrderTable);

        assertThat(actual.getNumberOfGuests()).isEqualTo(10);
    }
}
