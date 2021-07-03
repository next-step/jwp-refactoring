package kitchenpos.table.application;

import static kitchenpos.util.TestDataSet.테이블_1번;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("주문_테이블의 손님 수 정상 업데이트 케이스")
    void changeNumberOfGuests() {
        //given
        OrderTable change = new OrderTable(테이블_1번.getId(), 10, true);
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(change);
        given(orderTableDao.findById(any())).willReturn(Optional.of(테이블_1번));

        //when
        OrderTable result = tableService.changeEmpty(테이블_1번.getId(), change);

        // then
        assertThat(result.getId()).isEqualTo(change.getId());
        assertThat(result.getNumberOfGuests()).isEqualTo(change.getNumberOfGuests());

        verify(orderDao, times(1)).existsByOrderTableIdAndOrderStatusIn(any(), any());
        verify(orderTableDao, times(1)).save(any());

    }

    @Test
    @DisplayName("손님은 0명 미만일 경우 실패한다.")
    void underZero() {
        //given
        OrderTable change = new OrderTable(테이블_1번.getId(), -1, true);

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            tableService.changeNumberOfGuests(테이블_1번.getId(), change);
        });
    }

    @Test
    @DisplayName("주문_테이블의 빈 여부 업데이트 수정 성공 케이스")
    void changeEmpty() {
        //given
        OrderTable change = new OrderTable(테이블_1번.getId(), 10, false);
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(change);
        given(orderTableDao.findById(any())).willReturn(Optional.of(테이블_1번));

        //when
        OrderTable result = tableService.changeEmpty(테이블_1번.getId(), change);

        // then
        assertThat(result.getId()).isEqualTo(change.getId());
        assertThat(result.isEmpty()).isEqualTo(false);

        verify(orderDao, times(1)).existsByOrderTableIdAndOrderStatusIn(any(), any());
        verify(orderTableDao, times(1)).save(any());
        verify(orderTableDao, times(1)).findById(any());
    }

    @Test
    @DisplayName("존재 하지 않는 테이블을 업데이트 할 수 없다.")
    void noTable() {
        //given
        OrderTable change = new OrderTable(테이블_1번.getId(), 10, false);
        given(orderTableDao.findById(any())).willReturn(Optional.empty());

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            tableService.changeEmpty(테이블_1번.getId(), change);
            tableService.changeNumberOfGuests(테이블_1번.getId(), change);
        });

        verify(orderTableDao, times(1)).findById(any());
    }

    @Test
    @DisplayName("주문_테이블에 속한 주문들이 현재 조리중이거나, 식사 중일 시 테이블을 업데이트 할 수 없다.")
    void isEating() {
        //given
        OrderTable change = new OrderTable(테이블_1번.getId(), 10, true);
        given(orderTableDao.findById(any())).willReturn(Optional.of(테이블_1번));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(true);

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            tableService.changeEmpty(테이블_1번.getId(), change);
        });

        verify(orderTableDao, times(1)).findById(any());
        verify(orderDao, times(1)).existsByOrderTableIdAndOrderStatusIn(any(), any());
    }

}
