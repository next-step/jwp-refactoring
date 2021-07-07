package kitchenpos.table.application;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.application.TableService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블 생성 테스트")
    @Test
    void createTest() {
        // given
        OrderTable orderTable = new OrderTable(1l, 3);
        Mockito.when(orderTableDao.save(any())).thenReturn(orderTable);

        // when
        OrderTable actual = tableService.create(orderTable);

        // then
        assertThat(actual).isNotNull();
    }

    @DisplayName("전체 주문 테이블 리스트 조회 테스트")
    @Test
    void listTest() {
        // given
        OrderTable orderTable1 = new OrderTable(1l, 3);
        OrderTable orderTable2 = new OrderTable(1l, 3);
        Mockito.when(orderTableDao.findAll()).thenReturn(Arrays.asList(orderTable1, orderTable2));

        // when
        List<OrderTable> actual = tableService.list();

        // then
        assertThat(actual).isNotEmpty().hasSize(2);
    }

    @DisplayName("빈 테이블로 상태 변경 테스트")
    @Test
    void changeEmptyTest() {
        // given
        OrderTable orderTable = new OrderTable(null, 3);
        Mockito.when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));
        Mockito.when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(false);
        Mockito.when(orderTableDao.save(any())).thenReturn(orderTable);

        // when
        OrderTable actual = tableService.changeEmpty(1l, orderTable);

        // then
        assertThat(actual).isNotNull();
    }


    @DisplayName("주문 테이블의 손님 수 변경 테스트")
    @Test
    void changeNumberOfGuestsTest() {
        // given
        OrderTable orderTable = new OrderTable(1l, 3);
        Mockito.when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));
        Mockito.when(orderTableDao.save(any())).thenReturn(orderTable);

        // when
        OrderTable actual = tableService.changeNumberOfGuests(1l, orderTable);

        // then
        assertThat(actual).isNotNull();
    }

}
