package kitchenpos.table.application;

import static kitchenpos.table.application.TableServiceTestHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.TableService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    OrderTableDao orderTableDao;

    @Mock
    OrderDao orderDao;

    @InjectMocks
    TableService tableService;

    @DisplayName("좌석을 등록한다.")
    @Test
    void createTable() {
        // given
        OrderTable 좌석 = 좌석_정보(1L, 0, true, null);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(좌석);
        좌석 = tableService.create(좌석);

        // then
        assertThat(좌석).isNotNull();
    }

    @DisplayName("좌석을 목록을 조회한다.")
    @Test
    void listOrderTable() {
        // given
        OrderTable 좌석 = 좌석_정보(1L, 3, true, null);
        given(orderTableDao.findAll()).willReturn(Arrays.asList(좌석));
        List<OrderTable> 좌석_목록 = tableService.list();

        // then
        assertThat(좌석_목록.size()).isNotZero();
    }

    @DisplayName("좌석을 비운다.")
    @Test
    void changeEmptyOrderTable() {
        // given
        OrderTable 좌석 = 좌석_정보(1L, 3, false, null);
        OrderTable 빈_좌석 = 좌석_정보(1L, 0, true, null);

        given(orderTableDao.findById(any(Long.class))).willReturn(Optional.of(좌석));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(Long.class),
            anyList())).willReturn(false);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(빈_좌석);

        좌석 = tableService.changeEmpty(-1L, 좌석);

        // then
        assertThat(좌석.isEmpty()).isTrue();
    }

    @DisplayName("손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable 좌석 = 좌석_정보(1L, 3, false, null);

        given(orderTableDao.findById(any(Long.class))).willReturn(Optional.of(좌석));
        given(orderTableDao.save(any(OrderTable.class))).willReturn(좌석);

        좌석 = tableService.changeNumberOfGuests(1L, 좌석);

        // then
        assertThat(좌석.getNumberOfGuests()).isEqualTo(3);
    }
}
